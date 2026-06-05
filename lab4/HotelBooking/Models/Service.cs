using System.ComponentModel.DataAnnotations;

namespace HotelBooking.Models;

public class Service
{
    public int Id { get; set; }

    [Required, StringLength(200)]
    public string Name { get; set; } = "";

    [StringLength(500)]
    public string? Description { get; set; }

    [Range(0, 1_000_000)]
    public decimal Price { get; set; }

    // many-to-many: Service ↔ Booking (via BookingExtra)
    public ICollection<BookingExtra> BookingExtras { get; set; } = new List<BookingExtra>();
}
