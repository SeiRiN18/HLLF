# Рівень 1: 
# 3.Створіть програму, яка приймає два числа від користувача та виводить їх суму.

def calc_sum():
    print("Type 2 numbers")
    try:
        num1 = float(input('Enter first number '))
        num2 = float(input('Enter second number '))
        sum = num1 + num2
        print(f"Result of summarize is {sum}")
    except:
        print("Something went wrong")

# Рівень 2: 
# Реалізуйте програму, яка визначає, чи є введене користувачем число простим.

def is_prime_checker():
    number = int(input("Enter number "))
    if number < 2:
        print("Number is not prime")
        return
    else:
        is_prime = True

    for i in range(2, int(number ** 0.5) + 1):
        if number % i == 0:
            is_prime = False
    if is_prime:
        print("Number IS prime")
    else: 
        print("Number is not prime")


#Рівень 3: 
#3.	Створіть клас "Калькулятор" з методами для додавання, 
#віднімання, множення та ділення. 
#Виведіть результат обчислень для певного прикладу.
class Calculator:
    def add(self, a, b):
        return a + b
    def subtract(self, a, b):
        return a - b
    def multiply(self, a, b):
        return a * b
    def divide(self, a, b):
        if (b == 0):
            return "Division by zero not possible"
        return a / b

#Рівень 4:
#3.	Створіть клас "Книготека" з можливістю додавання 
#та видалення книг, а також виведення списку усіх книг.

class Library:
    def __init__(self):
        self.books = []
    def show_books(self):
        if len(self.books) == 0:
            print("Library is empty")
        else:
            print("Book list")
            for book in self.books:
                print("-", book)

    def add_book(self, book):
        self.books.append(book)
        print(f"Book '{book}' added")

    def remove_book(self, book):
        if book in self.books:
            self.books.remove(book)
            print(f"Book '{book}' removed")
        else:
            print("This book wasn't found")

if __name__ == "__main__":
    print("Task 1")
    calc_sum()


    print("Task 2")
    is_prime_checker()


    print("Task 3")
    calc = Calculator()
    print("Adding: ", calc.add(10, 20))
    print("Subtracting: ", calc.subtract(10, 20))
    print("Multiplying: ", calc.multiply(10, 20))
    print("Dividing: ", calc.divide(10, 20))


    print("Task 4")
    library = Library()

    library.add_book("Book1")
    library.show_books()

    library.add_book("Book2")
    library.show_books()
    
    library.remove_book("Book1")
    library.show_books()
    


