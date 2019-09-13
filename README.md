# Decomposition of relations for multi-model consistency preservation

This repository features an implementation of the **decomposition procedure**, an optimisation technique for multi-model consistency preservation. Simply explained, it transforms an unique consistency specification between various models into a set of independent, smaller consistency relations.

## Getting started

### Prerequisites

```
TODO :-)
```

### Installation

```
mvn clean install
```

## Presentation

### Decomposition procedure

An important task when maintaining several metamodels is to check that they are consistent with each other. Inconsistent metamodels can produce unexpected behaviors in software.
A consistency specification can be expressed as a set of consistency relations between metamodels.

Given a consistency network, the goal of my thesis is to develop a procedure to find out if this network can be simplified. Here, “simplify” means:

* Are there unnecessary relations that can be replaced by a combination of other relations?
* Is the network made of independent parts (which can then be maintained separately)?

This procedure is called “decomposition procedure”.

### Prototype

This implementation is a prototype of the decomposition procedure. The algorithm takes metamodels (*Ecore* files) and a consistency specification (*QVT-R* files) as input.

In essence, the steps of the algorithm are:

1. **Parse** QVT-R files and Ecore metamodels to generate an intermediate representation of the consistency specification.
2. **Translate** (OCL) consistency predicates in *QVTRelation*s into logical formulas and embed them in a graph.
3. **Solve** by using the graph to find candidate paths (≡ combinations of constraints to replace another constraint) and the Z3 solver to check the validity of these paths.

![Project organisation](https://raw.githubusercontent.com/aurelienpepin/KIT_ConsistencyPreservation_Decomposition/feature/cleancode/organisation.png?token=ABYBLDRPOF7RHHY37NRD6Y25QSBJA)

## Built with

* [Ecore](https://wiki.eclipse.org/Ecore), the core metamodel of the *Eclipse Modeling Framework*
* [QVTd](https://wiki.eclipse.org/MMT/QVT_Declarative_(QVTd)), an implementation of the QVT-R transformation language
* [Z3](https://github.com/Z3Prover/z3/wiki), a SMT solver
* [jGraphT](https://jgrapht.org/), a graph theory library 

## Authors

* [Aurélien Pepin](https:///github.com/aurelienpepin) – KIT, Ensimag

## See also

* An introduction to [multi-model consistency preservation](https://sdqweb.ipd.kit.edu/publications/pdfs/klare2018docsym.pdf)

## Development notes

```
mvn install:install-file -Dfile="src/main/resources/jar/com.microsoft.z3.jar" -DgroupId="com.microsoft"
-DartifactId="z3" -Dversion="4.8.5" -Dpackaging="jar" -DgeneratePom=true
```
