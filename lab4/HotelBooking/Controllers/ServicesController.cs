using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using HotelBooking.Data;
using HotelBooking.Models;

namespace HotelBooking.Controllers;

[ApiController]
[Route("api/[controller]")]
public class ServicesController : ControllerBase
{
    private readonly AppDbContext _db;
    public ServicesController(AppDbContext db) => _db = db;

    [HttpGet]
    public async Task<IActionResult> GetAll() =>
        Ok(await _db.Services.AsNoTracking().ToListAsync());

    [HttpGet("{id:int}")]
    public async Task<IActionResult> GetById(int id)
    {
        var svc = await _db.Services.FindAsync(id);
        return svc is null ? NotFound() : Ok(svc);
    }

    [HttpPost]
    public async Task<IActionResult> Create([FromBody] Service svc)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        _db.Services.Add(svc);
        await _db.SaveChangesAsync();
        return CreatedAtAction(nameof(GetById), new { id = svc.Id }, svc);
    }

    [HttpPut("{id:int}")]
    public async Task<IActionResult> Update(int id, [FromBody] Service dto)
    {
        if (!ModelState.IsValid) return BadRequest(ModelState);
        var svc = await _db.Services.FindAsync(id);
        if (svc is null) return NotFound();

        svc.Name        = dto.Name;
        svc.Description = dto.Description;
        svc.Price       = dto.Price;

        await _db.SaveChangesAsync();
        return Ok(svc);
    }

    [HttpDelete("{id:int}")]
    public async Task<IActionResult> Delete(int id)
    {
        var svc = await _db.Services.FindAsync(id);
        if (svc is null) return NotFound();
        _db.Services.Remove(svc);
        await _db.SaveChangesAsync();
        return NoContent();
    }
}
