# Java-RMI

**Idea**  
The project aims to facilitate the simultaneous creation of multiple histograms by multiple concurrent users. It involves building a distributed system where the server is responsible for constructing histograms, while users (clients) provide the data.

**Problem Description**  
The system enables users to generate multiple histograms concurrently. Users interact with the server to create histograms, provide data for the histograms, and retrieve histograms based on unique identifiers.

**Communication Protocol**  
Clients create histograms and receive unique histogram identifiers.
Clients submit data for histograms, including the corresponding histogram identifier to associate the data with the respective histogram.
Clients retrieve histograms using the specified histogram identifier.
The above operations can be executed concurrently by multiple clients.

**Assumptions**  
The service will be registered (bind) before initial usage.
A histogram will be created before any data submission.
Data submitted by the client will match the number of bins specified for the histogram (allowed from 0 to bins-1).
The getHistogram method will not be executed before the histogram is created.
Clients will only use valid histogram identifiers.
