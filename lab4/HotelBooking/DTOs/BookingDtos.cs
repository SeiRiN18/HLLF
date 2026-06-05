using System.ComponentModel.DataAnnotations;

namespace HotelBooking.DTOs;

public class CreateBookingDto
{
    [Required] public int      ClientId   { get; set; }
    [Required] public int      RoomId     { get; set; }
    [Required] public DateTime CheckIn    { get; set; }
    [Required] public DateTime CheckOut   { get; set; }
    public List<int> ServiceIds { get; set; } = new();
}

public class UpdateStatusDto
{
    [Required] public string Status { get; set; } = "";
}

public class BookingResponseDto
{
    public int      Id         { get; set; }
    public int      ClientId   { get; set; }
    public string   ClientName { get; set; } = "";
    public int      RoomId     { get; set; }
    public string   RoomInfo   { get; set; } = "";
    public DateTime CheckIn    { get; set; }
    public DateTime CheckOut   { get; set; }
    public decimal  TotalPrice { get; set; }
    public string   Status     { get; set; } = "";
    public DateTime CreatedAt  { get; set; }
    public List<string> Services { get; set; } = new();
}
