using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Caching.Memory;
using HotelBooking.Data;
using HotelBooking.Models;

namespace HotelBooking.Controllers;

[ApiController]
[Route("api/[controller]")]
public class HotelsController : ControllerBase
{
    private readonly AppDbContext  _db;
    private readonly IMemoryCache  _cache;
    private const string CACHE_KEY = "hotels_list";

    public HotelsController(AppDbContext db, IMemoryCache cache)
    {
        _db    = db;
        _cache = cache;
    }

    // Level 3: async + cache
    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        if (!_cache.TryGetValue(CACHE_KEY, out List<Hotel>? hotels))
        {
            hotels = await _db.Hotels
                .Include(h => h.Rooms)
                .AsNoTracking()
                .ToListAsync();

            _cache.Set(CACHE_KEY, hotels, TimeSpan.FromMinutes(5));
        }
        return Ok(hotels);
    }

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById(int id)
    {
        // Level 4: EF Core LINQ — параметризований запит, безпечний від SQL-ін'єкцій
        var hotel = await _db.Hotels
            .Include(h => h.Rooms)
            .AsNoTracking()
            .FirstOrDefaultAsync(h => h.Id == id);

        return hotel is null ? NotFound() : Ok(hotel);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Hotel hotel)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);

        _db.Hotels.Add(hotel);
        await _db.SaveChangesAsync();
        _cache.Remove(CACHE_KEY);

        return CreatedAtAction(nameof(GetById), new { id = hotel.Id }, hotel);
    }

    [HttpPut("{id:int}")]
    public async Task<IActionResult> Update(int id, [FromBody] Hotel dto)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);

        var hotel = await _db.Hotels.FindAsync(id);
        if (hotel is null) return NotFound();

        hotel.Name        = dto.Name;
        hotel.City        = dto.City;
        hotel.Address     = dto.Address;
        hotel.StarRating  = dto.StarRating;
        hotel.PhoneNumber = dto.PhoneNumber;
        hotel.Email       = dto.Email;

        await _db.SaveChangesAsync();
        _cache.Remove(CACHE_KEY);

        return Ok(hotel);
    }

    [HttpDelete("{id:int}")]
    public async Task<IActionResult> Delete(int id)
    {
        var hotel = await _db.Hotels.FindAsync(id);
        if (hotel is null) return NotFound();

        _db.Hotels.Remove(hotel);
        await _db.SaveChangesAsync();
        _cache.Remove(CACHE_KEY);

        return NoContent();
    }
}
