using Microsoft.EntityFrameworkCore;
using HotelBooking.Data;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new() { Title = "Hotel Booking API", Version = "v1" });
});

// Level 3: кешування в пам'яті
builder.Services.AddMemoryCache();

// EF Core + SQLite
builder.Services.AddDbContext<AppDbContext>(opts =>
    opts.UseSqlite(builder.Configuration.GetConnectionString("Default")
                   ?? "Data Source=hotelbooking.db"));

var app = builder.Build();

// Автоматично створити БД + seed при першому запуску
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AppDbContext>();
    db.Database.EnsureCreated();
}

app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "Hotel Booking API v1");
    c.RoutePrefix = string.Empty;
});

app.UseAuthorization();
app.MapControllers();
app.Run();
