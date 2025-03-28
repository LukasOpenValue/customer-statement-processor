# customer-statement-processor

Small program to validate MT940 type xml and csv files.

### How to build the application:

run ./gradlew build in the root directory of the project.

### How to build the application:

run ./gradlew bootRun in the root directory of the project.

### How to use the application:

The files are automatically picked up every five seconds from the ./data/input directory. Make sure this directory
exists in your execution directory. The error log is being put out into ./data/error and the processed files go to
./data/processed. There are also two REST endpoints available that take one file each that is then going to be processed
synchronously. The URLs for these POST endpoints are /api/files/upload/csv and /api/files/upload/xml.

### Some considerations on the design:

I believe in clean code and that documentation should be reflected in meaningful class, method and variable names, so
there is no java-doc to be found. If needed of course I could provide an extensive java-doc as well.
The solution using a set on the validator class to track the references was just made for time sake. I wanted a quick
way to check the references across files. In a more fleshed out version I would have used a database to store the
references and used spring batching to work through multiple files.
I only wanted to print the validation errors that were mentioned in the assignment into the report. There could be a
point made to have a parsing error file as well where things like missing values, wrong values and incorrect IBANs could
be logged.

