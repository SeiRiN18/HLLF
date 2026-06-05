using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Caching.Memory;
using HotelBooking.Data;
using HotelBooking.DTOs;
using HotelBooking.Models;

namespace HotelBooking.Controllers;

[ApiController]
[Route("api/[controller]")]
public class RoomsController : ControllerBase
{
    private readonly AppDbContext _db;
    private readonly IMemoryCache _cache;
    private const string CACHE_KEY = "rooms_available";

    public RoomsController(AppDbContext db, IMemoryCache cache)
    {
        _db    = db;
        _cache = cache;
    }

    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var rooms = await _db.Rooms
            .Include(r => r.Hotel)
            .AsNoTracking()
            .ToListAsync();
        return Ok(rooms);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById(int id)
    {
        var room = await _db.Rooms
            .Include(r => r.Hotel)
            .AsNoTracking()
            .FirstOrDefaultAsync(r => r.Id == id);
        return room is null ? NotFound() : Ok(room);
    }

    // Level 2: вільні кімнати за діапазоном дат
    // Level 3: async + cache
    // Level 4: LINQ-запит, параметризований EF Core — no SQL injection
    [HttpGet("available")]
    public async Task<IActionResult> GetAvailable(
        [FromQuery] DateTime checkIn,
        [FromQuery] DateTime checkOut,
        [FromQuery] int?     hotelId  = null,
        [FromQuery] int?     capacity = null)
    {
        if (checkIn >= checkOut)
            return BadRequest("Дата виїзду має бути пізніше дати заїзду");

        var cacheKey = $"{CACHE_KEY}_{checkIn:yyyyMMdd}_{checkOut:yyyyMMdd}_{hotelId}_{capacity}";

        if (!_cache.TryGetValue(cacheKey, out List<AvailableRoomDto>? result))
        {
            // Level 4: знаходимо зайняті кімнати через EF Core — автоматично параметризовано
            var occupiedIds = await _db.Bookings
                .Where(b => b.Status != BookingStatus.Cancelled &&
                            b.CheckIn  < checkOut &&
                            b.CheckOut > checkIn)
                .Select(b => b.RoomId)
                .Distinct()
                .ToListAsync();

            var query = _db.Rooms
                .Include(r => r.Hotel)
                .Where(r => !occupiedIds.Contains(r.Id))
                .AsNoTracking();

            if (hotelId.HasValue)  query = query.Where(r => r.HotelId  == hotelId.Value);
            if (capacity.HasValue) query = query.Where(r => r.Capacity >= capacity.Value);

            result = await query
                .Select(r => new AvailableRoomDto
                {
                    Id            = r.Id,
                    HotelId       = r.HotelId,
                    HotelName     = r.Hotel.Name,
                    HotelCity     = r.Hotel.City,
                    HotelStars    = r.Hotel.StarRating,
                    RoomNumber    = r.RoomNumber,
                    RoomType      = r.RoomType.ToString(),
                    PricePerNight = r.PricePerNight,
                    Capacity      = r.Capacity
                })
                .ToListAsync();

            _cache.Set(cacheKey, result, TimeSpan.FromMinutes(2));
        }

        return Ok(result);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Room room)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        _db.Rooms.Add(room);
        await _db.SaveChangesAsync();
        return CreatedAtAction(nameof(GetById), new { id = room.Id }, room);
    }

    [HttpPut("{id:int}")]
    public async Task<IActionResult> Update(int id, [FromBody] Room dto)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        var room = await _db.Rooms.FindAsync(id);
        if (room is null) return NotFound();

        room.RoomNumber    = dto.RoomNumber;
        room.RoomType      = dto.RoomType;
        room.PricePerNight = dto.PricePerNight;
        room.Capacity      = dto.Capacity;
        room.IsAvailable   = dto.IsAvailable;

        await _db.SaveChangesAsync();
        return Ok(room);
    }

    [HttpDelete("{id:int}")]
    public async Task<IActionResult> Delete(int id)
    {
        var room = await _db.Rooms.FindAsync(id);
        if (room is null) return NotFound();
        _db.Rooms.Remove(room);
        await _db.SaveChangesAsync();
        return NoContent();
    }
}
