# Order Management System

We chose Spring Boot because: It simplifies Spring setup, no XML config needed. Provides embedded server (Tomcat).
Supports REST API development easily.
Maven manages dependencies automatically. Standard for Java projects.

Layer	Responsibility
Controller Handles REST requests and responses
Service	Business logic, validation, processing
Repository	Data storage
Model	Data structure (Order, enums)
Exception	Custom exceptions for clean error handling
Analytics	Streams-based reports
Util	File I/O for logging

Models (Data Structure)
OrderType.java
public enum OrderType { BUY, SELL }
Enum ensures type safety → only BUY or SELL allowed.
Prevents invalid strings like "PURCHASE"
Cleaner than using constants (static final String).

OrderStatus.java
public enum OrderStatus { NEW, PROCESSING, COMPLETED, FAILED }

Order.java
orderId uniquely identifies each order, use UUID for uniqueness.
customerName identifies who placed the order.
type: BUY or SELL. amount: value of order. status: track processing status. createdTime: useful for analytics and debugging.

Why UUID over incremental IDs?
Safe in concurrent environments, no collision.

Why LocalDateTime?
Provides timestamp without needing external libraries.

Could use AtomicInteger for incremental ID → simpler, but unsafe if distributed or multi-threaded.

Custom Exception
public class InvalidOrderException extends RuntimeException { ... }
Clear separation of business logic errors from system errors.
Example: amount <= 0: throw InvalidOrderException.

Could use IllegalArgumentException → generic, not business-specific.

Repository Layer (In-Memory Storage)
private final List<Order> orders = new CopyOnWriteArrayList<>();
CopyOnWriteArrayList is safe for concurrent reads/writes.
Multiple threads can process orders without synchronization.

ArrayList is not thread-safe, could throw ConcurrentModificationException.

@Repository: Marks this as a repository bean managed by Spring IoC.
Spring can inject it into service classes automatically.

Could use ConcurrentHashMap<String, Order> → also thread-safe, but List preserves order of insertion.

File Logger (Java I/O)
try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders.log", true))) { ... }
BufferedWriter is more efficient than FileWriter, reduces disk I/O.
Wraps writer in buffer, writes in chunks.

try-with-resources auto-closes writer prevents memory leak.

append mode (true) avoids overwriting previous logs.

@Component marks it as a Spring-managed bean.
Can be injected anywhere using constructor injection.

Service Layer (Business Logic + Multithreading)
private final ExecutorService executor = Executors.newFixedThreadPool(5);
ExecutorService handles concurrent order processing.
Thread pool improves efficiency → limits number of threads.
Too many threads will be heavy on memory.
ExecutorService manages threads efficiently.

Analytics using Streams
repository.findAll().stream()
  .mapToDouble(Order::getAmount).sum();

Streams are functional, clean, less boilerplate.
Efficient for aggregate operations like sum, filter, groupBy.

@Service: Spring bean for business logic.
@PreDestroy: runs before app shutdown → cleanly shuts down ExecutorService.

Could use Spring’s @Async → works too, but ExecutorService gives more control.

REST Controller
@RestController	Combines @Controller + @ResponseBody → returns JSON directly.
@RequestMapping("/orders")	Base path for all endpoints.
@PostMapping	Handles POST requests.
@GetMapping	Handles GET requests.
@RequestParam	Maps query parameters to method arguments.

constructor injection ensures immutability of dependencies.
Easier to test than field injection.

Global Exception Handler
@RestControllerAdvice	Intercepts exceptions globally for all controllers.
@ExceptionHandler	Maps a specific exception type to a method.
Centralized error handling: no need to handle exceptions in every controller.
Cleaner response to clients.
Handle exceptions in each controller → repetitive, messy.

Generics	List<Order> → type-safe collections.
Multithreading	ExecutorService → concurrent processing.
Streams	Analytics → clean functional style.
File I/O	BufferedWriter → persistent logging.
Enums	Type safety for order type/status.
Custom Exceptions	Business-specific error handling.
Dependency Injection (DI)	Constructor injection → easier testing & loose coupling.
IoC (Inversion of Control)	Spring manages beans → no new needed for dependencies.

Thread-safety → CopyOnWriteArrayList.
Performance → BufferedWriter, ExecutorService thread pool.
Clean code → Streams, layered architecture.
Error handling → Custom exceptions + Global handler.
Spring best practices → @Service, @Repository, @Component, constructor injection.
Ease for beginners → Minimal external dependencies, self-contained OMS.
