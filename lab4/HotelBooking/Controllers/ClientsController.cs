using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using HotelBooking.Data;
using HotelBooking.Models;

namespace HotelBooking.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ClientsController : ControllerBase
{
    private readonly AppDbContext _db;
    public ClientsController(AppDbContext db) => _db = db;

    [HttpGet]
    public async Task<IActionResult> GetAll() =>
        Ok(await _db.Clients.AsNoTracking().ToListAsync());

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById(int id)
    {
        var client = await _db.Clients
            .Include(c => c.Bookings)
            .AsNoTracking()
            .FirstOrDefaultAsync(c => c.Id == id);
        return client is null ? NotFound() : Ok(client);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Client client)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);

        // Level 4: унікальність email через параметризований LINQ
        var exists = await _db.Clients.AnyAsync(c => c.Email == client.Email);
        if (exists) return Conflict("Email вже використовується");

        _db.Clients.Add(client);
        await _db.SaveChangesAsync();
        return CreatedAtAction(nameof(GetById), new { id = client.Id }, client);
    }

    [HttpPut("{id:int}")]
    public async Task<IActionResult> Update(int id, [FromBody] Client dto)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        var client = await _db.Clients.FindAsync(id);
        if (client is null) return NotFound();

        client.FirstName      = dto.FirstName;
        client.LastName       = dto.LastName;
        client.Email          = dto.Email;
        client.PhoneNumber    = dto.PhoneNumber;
        client.PassportNumber = dto.PassportNumber;

        await _db.SaveChangesAsync();
        return Ok(client);
    }

    [HttpDelete("{id:int}")]
    public async Task<IActionResult> Delete(int id)
    {
        var client = await _db.Clients.FindAsync(id);
        if (client is null) return NotFound();
        _db.Clients.Remove(client);
        await _db.SaveChangesAsync();
        return NoContent();
    }
}
