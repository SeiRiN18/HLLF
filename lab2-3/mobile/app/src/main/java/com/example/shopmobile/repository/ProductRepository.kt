package com.example.shopmobile.repository

import com.example.shopmobile.data.Product

object ProductRepository {

    private val products = listOf(
        Product("p1",  "Ноутбук Dell XPS 13",       "Електроніка", 45000, "Ультрабук з дисплеєм InfinityEdge 13.4\", процесором Intel Core i7 та 16 ГБ RAM.", 4.8, 5),
        Product("p2",  "Навушники Sony WH-1000XM5",  "Електроніка", 12000, "Бездротові навушники з активним шумозаглушенням та 30 год автономної роботи.",     4.9, 12),
        Product("p3",  "Смартфон iPhone 15",          "Електроніка", 38000, "Флагманський смартфон з чіпом A16 Bionic, 48 МП камерою та Dynamic Island.",       4.7, 8),
        Product("p4",  "Планшет iPad Pro 12.9",       "Електроніка", 52000, "Потужний планшет з чіпом M2, дисплеєм Liquid Retina XDR та Apple Pencil.",         4.8, 6),
        Product("p5",  "Зимова куртка Columbia",      "Одяг",         4500, "Тепла куртка з утеплювачем Omni-Heat та водовідштовхувальним покриттям.",           4.5, 20),
        Product("p6",  "Кросівки Nike Air Max 270",   "Одяг",         5500, "Стильні кросівки з амортизаційною підошвою Air Max для комфортного носіння.",       4.6, 15),
        Product("p7",  "JavaScript: Повний посібник", "Книги",         850, "Вичерпне керівництво по JavaScript для початківців та досвідчених розробників.",    4.9, 30),
        Product("p8",  "Clean Code",                  "Книги",         720, "Книга Роберта Мартіна про принципи написання чистого коду.",                        4.8, 25),
        Product("p9",  "Кавоварка DeLonghi",          "Дім",          7800, "Автоматична кавоварка з вбудованим кавомолком та функцією капучіно.",               4.7, 7),
        Product("p10", "Пилосос Dyson V15",           "Дім",         18000, "Безпровідний пилосос з лазерним виявленням пилу та 60 хв автономної роботи.",       4.9, 4)
    )

    fun getAll(): List<Product> = products

    fun getById(id: String): Product? = products.find { it.id == id }

    fun getCategories(): List<String> = products.map { it.category }.distinct()

    fun filter(query: String = "", category: String = ""): List<Product> =
        products.filter { p ->
            (query.isBlank() || p.name.contains(query, ignoreCase = true)) &&
            (category.isBlank() || p.category == category)
        }
}
