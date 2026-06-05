using System.ComponentModel.DataAnnotations;

namespace HotelBooking.Models;

public enum RoomType { Single, Double, Suite, Family }

public class Room
{
    public int Id { get; set; }

    // many-to-1: Room → Hotel
    public int HotelId { get; set; }
    public Hotel Hotel { get; set; } = null!;

    [Required, StringLength(10)]
    public string RoomNumber { get; set; } = "";

    public RoomType RoomType { get; set; }

    [Range(0.01, 1_000_000)]
    public decimal PricePerNight { get; set; }

    [Range(1, 20)]
    public int Capacity { get; set; }

    public bool IsAvailable { get; set; } = true;

    // 1-to-many: Room → Bookings
    public ICollection<Booking> Bookings { get; set; } = new List<Booking>();
}
