#Java-adamant

A library enhancing the JRE with the core functionality it should have had for a long time. 

The core ideas are: 

* __Collections are value objects and therefore immutable__ (persistent data stractures, changes create new collections, the originating one kept unchanged)
* `java.util.List` and the `util`-Collection-API just stay alive because of its wide spread not its quality
* __Collections are fully type-safe!__ (no `contains(Object)` and such)
* __Collections are thread-safe by concept__ (no additional API or impl., wrappers or runtime-exceptions)
* Eager evaludation is (in most cases) the appropriate way to solve problems in Java (even tho' we work immutable)
* `null` is avoided as far as possible - it is not considered as a valid value (e.g. as a collection's element)
* Interfaces should be narrow/minimal (1 method for most of em) - a rich set of utile functions comes from combining and composing minimal interfaces (maximized decoupling) 
* functions should be combinable with a minimal use of generics (specialised interfaces + adapters over single general + multiple generics)
* favour __pure functions__ (stateless functions, therefore often constants - code describes what to do when a value becomes available) 
* __Reflection is pain__ - concepts have to be compile-time save 
* There is no natural order - order depends on the point of view (`Comparable` is not a satisfactory solution)
* `Iterator` / `Iterable` is badly designed and needs a replacement (there is a 'legacy' support to integrate in common frameworks anyway)

### About
'jadamant' is a made-up word conbined out of 'Java' and 'Adamant'. Inspired by the Master of Orion II Adamantium plating. Adamant symbolises the immutable solid character and thereby the philosophy of this project.

### Status
The 0.1 beta is planed to be released somewhen 2012. Until then there might be major changes to the API. 
But the core collection API is nearly stable pretty much done implemented and covered with tests.

__Feal free to contact me and get to know more about the current state.__

### Notable facts about jadamant's persistent collections

* All collections are __immutable__ (read-only; changes create new objects, the originating one kept unchanged)
* All collections are __not__ `Iterable` ~ this concept got replaced too
* The API is index based or orientated (see the examples to get the difference)
* `List`s are the basic collection (there is no `Collection`-type)
* Often `List`s consists of a chain of partial `List`s
* A `Bag` is literally a `Sorted``List` 
* A `Set` is literally a special `Bag` (and therefore a `List` too) with a unique constraint 
* A `Map` __is literally__ a `Set` of `Entry`s, so it's a `Bag` too... and a `List`... and...
* a `Multimap` (a `Bag` of `Entry`s) or a map with a list of values for each key

This migth sound unfamiliar for a moment. Hopefully there will be a lot of examples soon to show why this is the better approch. 

### Working with Lists
``` java
// list construction
List.with.noElements(); // a empty list
List.with.element(1); // a list with a single element - the integer 1
List.with.elements(1,2,3,4); // a list holding the numbers 1 to 4
```

to be continued...

You should have a look to the unit-tests in the `de.jbee.lang` and `de.jbee.lang.seq` package to see the current API in use.