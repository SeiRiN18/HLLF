using System.ComponentModel.DataAnnotations;

namespace HotelBooking.Models;

public class Client
{
    public int Id { get; set; }

    [Required, StringLength(100)]
    public string FirstName { get; set; } = "";

    [Required, StringLength(100)]
    public string LastName { get; set; } = "";

    [Required, EmailAddress, StringLength(200)]
    public string Email { get; set; } = "";

    [Phone]
    public string? PhoneNumber { get; set; }

    [StringLength(20)]
    public string? PassportNumber { get; set; }

    // 1-to-many: Client → Bookings
    public ICollection<Booking> Bookings { get; set; } = new List<Booking>();
}
