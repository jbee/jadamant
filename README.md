#Java-adamant

A library enhancing the JRE with the core functionality it should have had for a long time. 

The core ideas are: 

* __Collections are value objects and therefore immutable__ (read-only; changes create new collections, the originating one kept unchanged)
* Eager evaludation is (in most cases) the appropriate way to solve a problem in Java (even tho' we work immutable)
* `null` is avoided as far as possible - it is not considered as a valid value (e.g. in collections)
* Interfaces should be narrow/minimal (1 method for most of em) - a rich set of utile functions comes from combining and composing minimal interfaces
* functions should be combinable with a minimal use of generics (specialised interfaces + adapters over single general + multiple generics)
* functions try to be stateless and therefore constants (attempt to write code that knows what to do when a value becomes available instead of doing it directly)                                                                                                                      
* Reflection is pain - it is replaced with concepts to avoid them wherever one is found 
* There is no natural order - order depends on the point of view (`Comparable` is not a satisfactory solution)
* `Iterator` / `Iterable` is badly designed and needs a replacement (there is a 'legacy' support to integrate in common frameworks anyway)

### About
'jadamant' is a made-up word conbined out of 'Java' and 'Adamant'. Inspired by the Master of Orion II Adamantium plating Adamant should symbolise the immutable and solid character and the hole philosophy of this project.

### Working with Lists
``` java
// list construction
List.with.noElements(); // a empty list
List.with.element(1); // a list with a single element - the integer 1
List.with.elements(1,2,3,4); // a list holding the numbers 1 to 4
```

to be continued...