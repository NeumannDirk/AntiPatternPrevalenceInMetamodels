# Antipattern-Analyzer for Meta-Models #

## Currently supported Metrics: ##
- Number of Classes
- Counting Complexity (number of all attributes, references, supertyp relations and operations)
- Conversion to a Hypergraph
  - counting nodes
  - counting hyperedes
  - calculating entropy

## Currently supported Anti-Pattern: ##
- Class Has More Than One ID [see here](https://sites.google.com/site/metamodelingantipatterns/catalog/mof/class-has-more-than-one-id)
- Malformed Multiplicity Element
  - complete, triggers on [*,y] or [x,y] with x > y 
  - restricted, triggers only on [x,y] with x > y 
- Unnamed Element [see here](https://sites.google.com/site/metamodelingantipatterns/catalog/mof/named-element-has-no-name)
  - complete, an `ENamedElement` has `null` or empty string as name
  - only not Packages, an `ENamedElement` **which is not an `EPackage`** has `null` or empty string as name
- Enumeration Has Attributes [see here](https://sites.google.com/site/metamodelingantipatterns/catalog/mof/enumeration-has-attributes)
  - an `EEnumeration` has an `EAttribute` as one of its contents
- Reference Has No Type [see here](https://sites.google.com/site/metamodelingantipatterns/catalog/mof/typed-element-has-no-type)
  - slightly variation only for `EReference` and `EAttribute`
  - excluding e.g. `EOperations` which are `ETypedElements` but might represent a procedure/"void" 
  - element has no type
- Diamond Inheritance
  - DiamondBottom -> DiamondLeft -> DiamondTop
  - DiamondBottom -> DiamondRight -> DiamondTop
  - -> DiamondBottom triggers the antipattern
- Class Not In Package Contained
  - an `EClass` is not contained in any `EPackage`
- Multiple Possible Containers
  - an instance of an `EClass` can be contained in multiple different containers (not at the same time though)
