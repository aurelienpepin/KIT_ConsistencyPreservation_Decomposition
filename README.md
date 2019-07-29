# Decomposition of relations for multi-model consistency preservation – Master's thesis at KIT

This repository features an implementation of the **decomposition procedure**, an optimisation technique for multi-model consistency preservation. Simply explained, it transforms an unique consistency specification between various models into a set of independent, smaller consistency relations.

---

## Getting started

### Prerequisites

```
TODO :-)
```

### Installation

```
TODO :-)
```

---

## Built with

* [Ecore](https://wiki.eclipse.org/Ecore), the core metamodel of the *Eclipse Modeling Framework*
* [QVTd](https://wiki.eclipse.org/MMT/QVT_Declarative_(QVTd)), an implementation of the QVT-R transformation language
* [Z3](https://github.com/Z3Prover/z3/wiki), a SMT solver
* [Maven](https://maven.apache.org/), a tool for dependency management

---

## Authors

* [Aurélien Pepin](https:///github.com/aurelienpepin) – KIT, Ensimag

---

## See also

* An introduction to [multi-model consistency preservation](https://sdqweb.ipd.kit.edu/publications/pdfs/klare2018docsym.pdf)

--

## Development notes

```
mvn install:install-file -Dfile="src/main/resources/jar/com.microsoft.z3.jar" -DgroupId="com.microsoft"
-DartifactId="z3" -Dversion="4.8.5" -Dpackaging="jar" -DgeneratePom=true
```
