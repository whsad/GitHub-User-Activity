# GitHub User Activity

## Project Overview

GitHub User Activity is a Java command-line tool that retrieves and displays the latest activity of a specified GitHub user. By calling the GitHub API, users can view activity records such as pushes, issues, comments, and more. This tool supports customizable options, allowing users to choose whether to display timestamps for the activities.

## Environment Setup

### System Requirements

- Java 1.8 or higher
- Maven 3.0 or higher

### Installing Java

1. Download and install the [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
2. Set the `JAVA_HOME` environment variable to point to the JDK installation path.
3. Ensure that the `bin` directory is included in the system `PATH` environment variable.

### Installing Maven

1. Download and install [Apache Maven](https://maven.apache.org/download.cgi).
2. Unzip the Maven archive and add the `bin` directory to the system `PATH` environment variable.
3. Verify the installation:
   ```bash
   mvn -v
   ```

## Compiling the Project

1. Clone the project to your local machine:
   ```bash
   git clone https://github.com/your-username/user_activity-cli.git
   ```
   
2. Navigate to the project directory:
   ```bash
   cd user_activity-cli
   ```

3. Compile the project using Maven:
   ```bash
   mvn clean install
   ```

   This command will download the required dependencies and compile the project.

## Running the Project

Use the following command to run the project, passing the GitHub username as an argument:

```bash
mvn exec:java -Dexec.mainClass="com.kirito.GitHubActivityFetcher" -Dexec.args="username --time"
```

- `username`: The GitHub username you want to query.
- `--time` (optional): Add this argument to display the timestamps of the activities.

### Example

```bash
mvn exec:java -Dexec.mainClass="com.kirito.GitHubActivityFetcher" -Dexec.args="octocat --time"
```

## Features

- **Fetch User Activity**: Displays the latest activity records of a specified GitHub user.
- **Timestamp Display**: Users can choose whether to display time information for the activities.
- **Supports Multiple Activity Types**: Includes various activity types such as pushes, issues, starring, forking, and more.
