using System.ComponentModel.DataAnnotations;

namespace HotelBooking.Models;

public class Hotel
{
    public int Id { get; set; }

    [Required, StringLength(200)]
    public string Name { get; set; } = "";

    [Required, StringLength(100)]
    public string City { get; set; } = "";

    [Required, StringLength(300)]
    public string Address { get; set; } = "";

    [Range(1, 5)]
    public int StarRating { get; set; }

    [Phone]
    public string? PhoneNumber { get; set; }

    [EmailAddress, StringLength(200)]
    public string? Email { get; set; }

    // 1-to-many: Hotel → Rooms
    public ICollection<Room> Rooms { get; set; } = new List<Room>();
}
