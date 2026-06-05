using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using HotelBooking.Data;
using HotelBooking.DTOs;
using HotelBooking.Models;

namespace HotelBooking.Controllers;

[ApiController]
[Route("api/[controller]")]
public class BookingsController : ControllerBase
{
    private readonly AppDbContext _db;
    public BookingsController(AppDbContext db) => _db = db;

    // Level 3: async + eager loading
    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var bookings = await _db.Bookings
            .Include(b => b.Client)
            .Include(b => b.Room).ThenInclude(r => r.Hotel)
            .Include(b => b.BookingExtras).ThenInclude(be => be.Service)
            .AsNoTracking()
            .Select(b => ToDto(b))
            .ToListAsync();

        return Ok(bookings);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById(int id)
    {
        var b = await _db.Bookings
            .Include(b => b.Client)
            .Include(b => b.Room).ThenInclude(r => r.Hotel)
            .Include(b => b.BookingExtras).ThenInclude(be => be.Service)
            .AsNoTracking()
            .FirstOrDefaultAsync(b => b.Id == id);

        return b is null ? NotFound() : Ok(ToDto(b));
    }

    [HttpGet("client/{clientId:int}")]
    public async Task<IActionResult> GetByClient(int clientId)
    {
        var bookings = await _db.Bookings
            .Include(b => b.Room).ThenInclude(r => r.Hotel)
            .Include(b => b.BookingExtras).ThenInclude(be => be.Service)
            .Where(b => b.ClientId == clientId)
            .AsNoTracking()
            .Select(b => ToDto(b))
            .ToListAsync();

        return Ok(bookings);
    }

    // Level 2: створення бронювання з перевіркою доступності
    // Level 4: всі запити — LINQ, параметризовані EF Core (захист від SQL-ін'єкцій)
    [HttpPost]
    public async Task<IActionResult> Create([FromBody] CreateBookingDto dto)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);

        if (dto.CheckIn.Date < DateTime.UtcNow.Date)
            return BadRequest("Дата заїзду не може бути в минулому");

        if (dto.CheckIn >= dto.CheckOut)
            return BadRequest("Дата виїзду має бути пізніше дати заїзду");

        // Level 4: перевірка конфлікту через параметризований LINQ-запит
        var isOccupied = await _db.Bookings.AnyAsync(b =>
            b.RoomId == dto.RoomId &&
            b.Status != BookingStatus.Cancelled &&
            b.CheckIn  < dto.CheckOut &&
            b.CheckOut > dto.CheckIn);

        if (isOccupied)
            return Conflict("Кімната вже зайнята на ці дати");

        var room   = await _db.Rooms.FindAsync(dto.RoomId);
        if (room is null) return BadRequest("Кімнату не знайдено");

        var client = await _db.Clients.FindAsync(dto.ClientId);
        if (client is null) return BadRequest("Клієнта не знайдено");

        int     nights = (dto.CheckOut.Date - dto.CheckIn.Date).Days;
        decimal total  = room.PricePerNight * nights;

        if (dto.ServiceIds.Any())
        {
            var servicePrices = await _db.Services
                .Where(s => dto.ServiceIds.Contains(s.Id))
                .SumAsync(s => s.Price);
            total += servicePrices;
        }

        var booking = new Booking
        {
            ClientId   = dto.ClientId,
            RoomId     = dto.RoomId,
            CheckIn    = dto.CheckIn.Date,
            CheckOut   = dto.CheckOut.Date,
            TotalPrice = total,
            Status     = BookingStatus.Confirmed
        };

        _db.Bookings.Add(booking);
        await _db.SaveChangesAsync();

        foreach (var sid in dto.ServiceIds.Distinct())
            _db.BookingExtras.Add(new BookingExtra { BookingId = booking.Id, ServiceId = sid });

        await _db.SaveChangesAsync();
        return CreatedAtAction(nameof(GetById), new { id = booking.Id }, booking);
    }

    [HttpPatch("{id:int}/status")]
    public async Task<IActionResult> UpdateStatus(int id, [FromBody] UpdateStatusDto dto)
    {
        var booking = await _db.Bookings.FindAsync(id);
        if (booking is null) return NotFound();

        if (!Enum.TryParse<BookingStatus>(dto.Status, ignoreCase: true, out var newStatus))
            return BadRequest($"Невірний статус. Допустимі: {string.Join(", ", Enum.GetNames<BookingStatus>())}");

        booking.Status = newStatus;
        await _db.SaveChangesAsync();
        return Ok(booking);
    }

    // Level 2: скасування бронювання
    [HttpDelete("{id:int}")]
    public async Task<IActionResult> Cancel(int id)
    {
        var booking = await _db.Bookings.FindAsync(id);
        if (booking is null) return NotFound();

        booking.Status = BookingStatus.Cancelled;
        await _db.SaveChangesAsync();
        return NoContent();
    }

    private static BookingResponseDto ToDto(Booking b) => new()
    {
        Id         = b.Id,
        ClientId   = b.ClientId,
        ClientName = $"{b.Client.FirstName} {b.Client.LastName}",
        RoomId     = b.RoomId,
        RoomInfo   = $"{b.Room.Hotel.Name} — кімн. {b.Room.RoomNumber}",
        CheckIn    = b.CheckIn,
        CheckOut   = b.CheckOut,
        TotalPrice = b.TotalPrice,
        Status     = b.Status.ToString(),
        CreatedAt  = b.CreatedAt,
        Services   = b.BookingExtras.Select(be => be.Service.Name).ToList()
    };
}
