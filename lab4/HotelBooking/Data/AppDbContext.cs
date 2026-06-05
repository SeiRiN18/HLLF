using Microsoft.EntityFrameworkCore;
using HotelBooking.Models;

namespace HotelBooking.Data;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

    public DbSet<Hotel>       Hotels       { get; set; }
    public DbSet<Room>        Rooms        { get; set; }
    public DbSet<Client>      Clients      { get; set; }
    public DbSet<Booking>     Bookings     { get; set; }
    public DbSet<Service>     Services     { get; set; }
    public DbSet<BookingExtra> BookingExtras { get; set; }

    protected override void OnModelCreating(ModelBuilder mb)
    {
        // ── Relationships ──────────────────────────────────────────
        // 1-to-many: Hotel → Rooms
        mb.Entity<Room>()
            .HasOne(r => r.Hotel)
            .WithMany(h => h.Rooms)
            .HasForeignKey(r => r.HotelId)
            .OnDelete(DeleteBehavior.Cascade);

        // 1-to-many: Client → Bookings
        mb.Entity<Booking>()
            .HasOne(b => b.Client)
            .WithMany(c => c.Bookings)
            .HasForeignKey(b => b.ClientId)
            .OnDelete(DeleteBehavior.Restrict);

        // 1-to-many: Room → Bookings
        mb.Entity<Booking>()
            .HasOne(b => b.Room)
            .WithMany(r => r.Bookings)
            .HasForeignKey(b => b.RoomId)
            .OnDelete(DeleteBehavior.Restrict);

        // many-to-many: Booking ↔ Service via BookingExtra
        mb.Entity<BookingExtra>()
            .HasKey(be => new { be.BookingId, be.ServiceId });

        mb.Entity<BookingExtra>()
            .HasOne(be => be.Booking)
            .WithMany(b => b.BookingExtras)
            .HasForeignKey(be => be.BookingId);

        mb.Entity<BookingExtra>()
            .HasOne(be => be.Service)
            .WithMany(s => s.BookingExtras)
            .HasForeignKey(be => be.ServiceId);

        // ── Indexes (Level 3: query optimisation) ──────────────────
        mb.Entity<Room>()
            .HasIndex(r => r.HotelId);

        mb.Entity<Booking>()
            .HasIndex(b => b.ClientId);

        mb.Entity<Booking>()
            .HasIndex(b => b.RoomId);

        mb.Entity<Booking>()
            .HasIndex(b => new { b.CheckIn, b.CheckOut });

        mb.Entity<Client>()
            .HasIndex(c => c.Email)
            .IsUnique();

        // ── Decimal precision ──────────────────────────────────────
        mb.Entity<Room>()    .Property(r => r.PricePerNight).HasPrecision(10, 2);
        mb.Entity<Booking>() .Property(b => b.TotalPrice)   .HasPrecision(10, 2);
        mb.Entity<Service>() .Property(s => s.Price)        .HasPrecision(10, 2);

        // ── Seed data ──────────────────────────────────────────────
        mb.Entity<Hotel>().HasData(
            new Hotel { Id = 1, Name = "Харків Палас",    City = "Харків", Address = "вул. Сумська 6",         StarRating = 5, Email = "info@hpalace.ua",  PhoneNumber = "+380577001000" },
            new Hotel { Id = 2, Name = "Ramada Lviv",     City = "Львів",  Address = "вул. Городоцька 254",    StarRating = 4, Email = "info@ramada.ua",   PhoneNumber = "+380322001000" },
            new Hotel { Id = 3, Name = "Reikartz Kyiv",   City = "Київ",   Address = "вул. Антоновича 2/4",   StarRating = 4, Email = "kyiv@reikartz.ua", PhoneNumber = "+380442001000" }
        );

        mb.Entity<Room>().HasData(
            new Room { Id = 1, HotelId = 1, RoomNumber = "101", RoomType = RoomType.Single, PricePerNight = 1200, Capacity = 1, IsAvailable = true },
            new Room { Id = 2, HotelId = 1, RoomNumber = "201", RoomType = RoomType.Double, PricePerNight = 2200, Capacity = 2, IsAvailable = true },
            new Room { Id = 3, HotelId = 1, RoomNumber = "301", RoomType = RoomType.Suite,  PricePerNight = 5000, Capacity = 2, IsAvailable = true },
            new Room { Id = 4, HotelId = 2, RoomNumber = "101", RoomType = RoomType.Single, PricePerNight = 1000, Capacity = 1, IsAvailable = true },
            new Room { Id = 5, HotelId = 2, RoomNumber = "201", RoomType = RoomType.Double, PricePerNight = 1800, Capacity = 2, IsAvailable = true },
            new Room { Id = 6, HotelId = 3, RoomNumber = "501", RoomType = RoomType.Family, PricePerNight = 3500, Capacity = 4, IsAvailable = true }
        );

        mb.Entity<Service>().HasData(
            new Service { Id = 1, Name = "Сніданок",      Price = 200, Description = "Шведський стіл" },
            new Service { Id = 2, Name = "Трансфер",      Price = 500, Description = "Аеропорт — готель" },
            new Service { Id = 3, Name = "СПА",           Price = 800, Description = "1 година" },
            new Service { Id = 4, Name = "Паркінг",       Price = 150, Description = "За добу" },
            new Service { Id = 5, Name = "Ранній заїзд",  Price = 300, Description = "З 08:00" }
        );
    }
}
