# pbb-collection-utilities

A lightweight, high-performance, and pluggable Java utility module for frequency distribution analysis.

---

## 🚀 How To Use It

This module provides a dual-facade API tailored to handle different analytical needs depending on how you wish to process element occurrences.

### 1. Extracting All Frequency Leaders (The Raw Approach)
If your only requirement is to extract all the most frequent elements from a collection, the rationale is to use the lightweight static method layer. This approach makes zero assumptions about the data structure, guarantees no data loss, and avoids arbitrary truncations.

```java
import com.pbb.CollectionUtilities;

import java.util.List;
import java.util.Set;

List<String> votes = List.of("apple", "banana", "apple", "cherry", "banana");

// Instantly extracts all peak elements without any internal tie-breaking
Set<String> leaders = CollectionUtilities.findMostFrequent(votes);

// Output will contain both "apple" and "banana" (2 occurrences each)
System.out.println("Current leaders: " + leaders);

/* 
Expected Console Output:
Current leaders: [apple, banana]
*/
```

### 2. Uniqueness Analysis Pipeline (The Advanced Approach)
Finding a unique frequency leader involves additional issues related to its uniqueness analysis and therefore is provided in a more advanced workflow using the pipeline syntax. If your business logic depends on an undisputed winner, this pipeline executes the heavy calculations once and allows you to safely evaluate the results through conditional polling diagnostics without repeating expensive $O(n)$ loops.

```java
import com.pbb.CollectionUtilities;
import com.pbb.FrequencyResult;

import java.util.List;

List<String> items = List.of("apple", "banana", "apple", "cherry");

// 1. Trigger the advanced pipeline using the source blueprint (computes the distribution once)
FrequencyResult<String> fre = CollectionUtilities.source(items).analyzeFrequency();

// 2. Safely perform your uniqueness checks in O(1) against the immutable record
if (fre.hasUniqueLeader()) {
    // getLeader() makes it clear we are fetching the top occurrence winner
    String clearWinner = fre.getLeader();
    System.out.println("Undisputed Winner: " + clearWinner);
} else {
    // getLeaders() immediately indicates we are getting the tied frequency leaders
    System.out.println("Analysis inconclusive due to a tie between: " + fre.getLeaders());
}
```

---

## 🔌 Integration, Compilation & Plugin Configuration

No framework setup required. This module is a pure, self-contained Java library designed to plug directly into your project and just work.

### 1. Local Build & Packaging
To package this module into a shareable, standalone `.jar` file for your team, run the standard Maven lifecycle command from your terminal:
```bash
mvn clean package
```
This generates `pbb-collection-utilities-1.0-SNAPSHOT.jar` inside your local `target/` directory.

### 2. Local Repository Installation
To test this pluggable module locally across different projects on your workstation, install it into your local `.m2` repository:
```bash
mvn clean install
```

### 3. Client Dependency Declaration
Once published to a local or remote binary repository, consumers can plug the module directly into their applications by adding the following coordinates to their host configuration:

**For Maven projects (`pom.xml`):**
```xml
<dependency>
    <groupId>com.pbb.utilities</groupId>
    <artifactId>pbb-collection-utilities</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**For Gradle projects (`build.gradle`):**
```groovy
implementation 'com.pbb.utilities:pbb-collection-utilities:1.0-SNAPSHOT'
```

### 4. Continuous Integration & Continuous Deployment (CI/CD) Rationale
To guarantee the ongoing architectural integrity of this module without runtime framework overhead, the CI/CD pipeline operates on a strict automated verification strategy:
* **Decoupled Verification Lifecycle:** The pipeline runs `mvn clean test` on every code check-in or pull request. The inclusion of the decoupled `maven-surefire-plugin` ensures that unit tests run completely standalone in headless server environments.
* **Zero Overhead Constraints:** The deployment phase prevents external test artifacts or mocking frameworks from polluting the final compiled library, verifying that only raw, framework-free production bytecode is ever delivered to the central repository.
* **Deterministic Version Locking:** Pull requests targeting major branches trigger automated semantic version checks to guarantee that client microservices consuming the pluggable JAR never suffer from unmanaged breaking binary adjustments.

---

## 🛠️ Architecture & Core Philosophy
This module is explicitly built to be **framework-free** and **zero-dependency**. It completely excludes heavy frameworks like Spring Boot or Micronaut to maintain an ultra-minimal binary footprint. It complies strictly with **Java 24+**.

### The Non-Uniqueness Problem Design Choice
This solution is designed to prevent implicit bugs and silent data truncation when multiple elements share the highest occurrence count. It deliberately avoids relying on the collection's initial insertion order or natural sorting as an explicit tie-resolver, as doing so would occult the underlying logic upon which the correctness of this functionality depends.

Instead, pbb-collection-utilities treats frequency analysis as a transparent operational lifecycle:
* It ensures that the base operation (`findMostFrequent`) only returns raw, untruncated facts by exposing all matching peak elements.
* It requires the consumer to enter an explicit, type-safe workflow (`FrequencyResult` pipeline) whenever their business logic demands the validation of a single, unique winner.

#### Floating-Point Numbers Restriction
This version of the library disallows the use of floating-point numbers (`Float`, `Double`). Processing these types requires a more advanced analysis without which unconditional correctness of the frequency results cannot be guaranteed. This support may be introduced in future versions of the library. For now, an exception is thrown if any element of these types is detected within the processed collection.

---

## 🧪 Technical Specifications
* **Time Complexity:** Both approaches operate in strict **$O(n)$** time complexity for a single frequency mapping pass.
* **Space Complexity:** Requires **$O(d)$** auxiliary space, where $d$ represents the number of distinct elements inside the collection.
* **Null Safety:** All API layers are designed to be explicitly null-safe. Passing `null` or an empty collection will gracefully return empty sets or an empty result state without throwing runtime exceptions.
* **Thread-Safety & Concurrency:** The engine provides thread-safe execution boundaries through an absolute immutability strategy. The static utility `findMostFrequent` is entirely stateless, confining its working memory inside the thread stack. All pipeline blueprints initiated via `source` are strictly immutable, always returning a brand-new object on subsequent calls. Finally, the terminal pass compiles data into a strictly unmodifiable `FrequencyResult` record, allowing thread-safe, unrestricted concurrent polling without locking overhead.
