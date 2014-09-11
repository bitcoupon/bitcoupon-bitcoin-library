## Architectural diagrams

!!!**These diagrams and textual descriptions are under construction and will continuously be changed.**!!!

While this document was created it was assumed that Ruby on Rails and Android were chosen as development enviroments.

### Blackbox diagram for client-server data flow:

![Blackbox diagram for mobile and web app](blackbox.png 'Blackbox diagram for mobile and web app
')

Ruby on Rails web framework is defined as an MVC (Model-View-Controller) architecture. The client sends a request to the server. The request is interpreted by the router and directed to the associated controller. The controller delegates the work to be done and makes sure that the view is invoked with updated data from the model. The model takes care of persistence with the database and makes sure the data is valid. The bitcoin actions is also invoked by the model, because only the model contains the verified and correct data attributes. When the model has done its' tasks, the controller invokes the view which sends a response to the client; JSON response if mobile client, HTML/JS/CSS if browser client.

### Overall Deployment diagram:

![Overall Deployment diagram](deployment-arch.png 'Overall Deployment diagram
')

The diagram represent an overview of how the major modules are distributed in the client-server system. 

The server serves a RESTful API to be used by the mobile applications and serves the requested subset of the web application to the admin user. The server also contains a bitcoin module to perform bitcoin related tasks.

The mobile user has its mobile app and a bitcoin module which can create transactions and send transactions. This is all wrapped up in a app bundle, this is not implicit in the diagram. (TODO: Some container boxes around the artifacts?).

The database should be separated from the server, but if we choose a model where each group/organization creates their own community and closed network, it should be all set up on the same server. It's then kind of a deal-breaker to make this product easy to set up. 

#### To consider

Should signing a bitcoin be done on the admin client or on the server? If done on the server, the key should be encrypted when communicated over the wire. This decision is depended on another decision that has to be made; On which server should the system run? on each organization's server or on a server hosted by us? 

## Tools used

This section describes which tools beeing used for each type of diagram. This is only a suggestion right now. Programs used, will be in the dropbox folder, at `Arch-mockup/programs/` folder. Their respective names are listed below.

### Information flow (BPMN)
Bizagi Modeler

### Sequence diagrams, class diagrams, activity diagrams
Violet UML Editor

### Deployment diagrams, Blackbox
UMLet

### Database model (ER-models)
Visual ER

### DB queries

Relasjonsalgebra