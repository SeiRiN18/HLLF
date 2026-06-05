using System.ComponentModel.DataAnnotations;

namespace HotelBooking.Models;

public enum BookingStatus { Pending, Confirmed, CheckedIn, CheckedOut, Cancelled }

public class Booking
{
    public int Id { get; set; }

    // many-to-1: Booking → Client
    public int ClientId { get; set; }
    public Client Client { get; set; } = null!;

    // many-to-1: Booking → Room
    public int RoomId { get; set; }
    public Room Room { get; set; } = null!;

    [Required]
    public DateTime CheckIn { get; set; }

    [Required]
    public DateTime CheckOut { get; set; }

    public decimal TotalPrice { get; set; }

    public BookingStatus Status { get; set; } = BookingStatus.Pending;

    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

    // many-to-many: Booking ↔ Service (via BookingExtra)
    public ICollection<BookingExtra> BookingExtras { get; set; } = new List<BookingExtra>();
}
