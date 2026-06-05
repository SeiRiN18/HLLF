namespace HotelBooking.Models;

// Join table for many-to-many: Booking ↔ Service
public class BookingExtra
{
    public int BookingId { get; set; }
    public Booking Booking { get; set; } = null!;

    public int ServiceId { get; set; }
    public Service Service { get; set; } = null!;

    public int Quantity { get; set; } = 1;
}
