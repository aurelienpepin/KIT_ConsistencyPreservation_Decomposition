# Decomposition of relations for multi-model consistency preservation

This repository features an implementation of the **decomposition procedure**, an optimisation technique for multi-model consistency preservation. Simply explained, it transforms an unique consistency specification between various models into a set of independent, smaller consistency relations.

## Getting started

### Prerequisites

```
JDK >= 1.8
Maven >= 3.6.1
```

### Installation

```shell
mvn clean install
```

See [*Notes on installation*](#notes-on-installation) for further details.

### Test

```shell
mvn verify

# Code coverage (report in ./target/site/clover/index.html)
mvn clean clover:setup verify clover:aggregate clover:clover

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

## Notes on installation

This prototype relies on many external JARs, i.e. dependencies that are not available in Maven Public Repositories.
An in-project repository for custom dependencies (`repo`) has been set up in `src/main/resources`.

Executing `mvn clean install` is enough to resolve dependencies from public *and* custom repositories.
This is currently the best approach to distribute local dependencies with their source.

The only disadvantage is that these non-published dependencies will not be solved if this project is itself included into another project.
More details and solutions on this approach can be found [here](https://stackoverflow.com/a/7623805/8804793).
