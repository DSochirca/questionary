Client: ![Client coverage](https://gitlab.ewi.tudelft.nl/cse1105/2019-2020/organisation/repository-template/badges/master/coverage.svg?job=client-test)
Server: ![Server coverage](https://gitlab.ewi.tudelft.nl/cse1105/2019-2020/organisation/repository-template/badges/master/coverage.svg?job=server-test)


# Welcome to Questionary!

**Questionary** is a modern system which can allow the lecturers at TU Delft to teach their students to their utmost efficiency and effectiveness. _Lecturers_ can create _rooms_ for students, create _quizzes_ and see how students feel about the _lecture's speed_. _Students_ can join rooms and _ask questions_ they have about the lecture, as well as answering quizzes and voting the lecture's speed. Both _moderators and lecturers_ have privileged access to the rooms, and they can _edit, delete, mark questions as answered, and even ban ip addresses_.

## Description of project

**Questionary** is developed by students from **TU Delft** to make our lecturers' lives easier during the online teaching period. The system is general enough to be applicable to other educational institutions as well. Questionary uses a powerful and comprehensive **REST API built with Spring Boot**, as well as **JavaFX** as its UI platform.

Questionary may have reached version 1.1, but we're not stopping there. We have many feature ideas on our scrum board that we're anxious to add and other innovative workflows that we're planning to build into Questionary. So take Questionary out for a spin and let us know how we can make it your favorite app for your institution!

## Group members

| 📸 | Name | Email |
|---|---|---|
| ![](https://eu.ui-avatars.com/api/?name=SV&length=2&size=50&color=FFF&background=0D8ABC&font-size=0.325) | Stan Verlaan | S.Verlaan@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=LR&length=2&size=50&color=DDD&background=777&font-size=0.325) | Lukas Roels | l.n.w.roels@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=JM&length=2&size=50&color=AAA&background=920&font-size=0.325) | Jason Miao | y.miao-2@student.tudelft.nl || ![](https://eu.ui-avatars.com/api/?name=DS&length=2&size=50&color=FFF&background=006a4e&font-size=0.325) | Dan Sochirca | D.Sochirca@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=PS&length=2&size=50&color=DDD&background=006a4e&font-size=0.325) | Pablo Sacristan | P.SacristandelJunco@student.tudelft.nl |
| ![](https://eu.ui-avatars.com/api/?name=SM&length=2&size=50&color=FFF&background=26b6f0&font-size=0.325) | Sebastian Manda | S.A.Manda-1@student.tudelft.nl |

## How to run it

Firstly, **clone** the repository or **download** the source code. Then open the repository in you preferred **IDE** and **build the project**. Once the project's build is done, you can run the server from `server/src/main/java/nl/tudelft/oopp/demo/DemoAplication.java`, and the client from `client/src/main/java/nl/tudelft/oopp/demo/MainApp.java`. 

The app is configured to run on **localhost** only. If you want it to be accessible from outside your local machine you should configure port forwarding and set the server's ip address and port in the server's application properties. Also, you need to change the requests' ip address (or server's domain, if you're using one) in ServerCommunication

## How to contribute to it
The main purpose of this repository is to continue improving Questionary. We want to make contributing to this project as easy and transparent as possible, and we are grateful to the community for contributing bug fixes and improvements. Read below to learn how you can take part in improving Questionary.

### [Code of Conduct](CODE_OF_CONDUCT.md)
Questionary has adopted a Code of Conduct that we expect project participants to adhere to. Please read the [full text](CODE_OF_CONDUCT.md) so that you can understand what actions will and will not be tolerated.

There are many ways in which you can participate in the project, for example:
- Submit bugs and issues, and help us verify as they are checked in
- Review source code changes
- Review the documentation and make merge requests for anything from typos to new content

If you are interested in fixing issues and contributing directly to the code base, please read the **Contribution Guidelines** down below:

### Contribution Guidelines

Please ensure your merge request adheres to the following guidelines:

- Alphabetize your entry.
- Search previous merge requests before adding a new one, as yours may be a duplicate.
- Suggested READMEs should be beautiful or stand out in some way.
- Make an individual merge request for each suggestion.
- New categories, or improvements to the existing categorization are welcome.
- Keep descriptions short and simple, but descriptive.
- Start the description with a capital and end with a full stop/period.
- Check your spelling and grammar.
- Make sure your text editor is set to remove trailing whitespace.
- Test your code! Tests help us prevent regressions from being introduced to the codebase.

Thank you for your suggestions!

## Copyright / License 
Copyright (c) 2021 OOPP-GROUP-57. All rights reserved.

Licensed under the [MIT](LICENSE) license.
