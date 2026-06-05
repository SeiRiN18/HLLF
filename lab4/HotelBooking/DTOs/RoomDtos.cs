namespace HotelBooking.DTOs;

public class AvailableRoomDto
{
    public int     Id            { get; set; }
    public int     HotelId       { get; set; }
    public string  HotelName     { get; set; } = "";
    public string  HotelCity     { get; set; } = "";
    public int     HotelStars    { get; set; }
    public string  RoomNumber    { get; set; } = "";
    public string  RoomType      { get; set; } = "";
    public decimal PricePerNight { get; set; }
    public int     Capacity      { get; set; }
}
