Projeto Maven Resumo:
Spring Boot: versão 3.3.4

Esta versão do Spring Boot é definida pela dependência spring-boot-starter-parent, que gerencia as versões dos componentes Spring.
Java: versão 17

O projeto está configurado para usar a versão 17 do Java, que é a versão de LTS (Long Term Support).
Dependências e Versões:
Spring Boot Data JPA: (gerenciado pelo Spring Boot 3.3.4)

Usado para persistência de dados e interação com o banco de dados via JPA (Java Persistence API).
Spring Boot Security: (gerenciado pelo Spring Boot 3.3.4)

Usado para configurar segurança e autenticação no projeto.
Spring Boot Thymeleaf: (gerenciado pelo Spring Boot 3.3.4)

Usado para renderizar templates HTML com Thymeleaf.
Spring Boot Validation: (gerenciado pelo Spring Boot 3.3.4)

Usado para validação de dados no projeto, como formulários e entidades.
Spring Boot Web: (gerenciado pelo Spring Boot 3.3.4)

Usado para criar e gerenciar endpoints HTTP e APIs REST.
Thymeleaf Extras Spring Security 6: versão 6.x

Usado para integrar Thymeleaf com o Spring Security, facilitando o controle de permissões nas views.
Spring Boot DevTools: (gerenciado pelo Spring Boot 3.3.4)

Usado para facilitar o desenvolvimento com recarregamento automático e outras funcionalidades úteis.
Spring Boot Test: (gerenciado pelo Spring Boot 3.3.4)

Usado para testar a aplicação, incluindo testes de integração e unitários.
MySQL Connector: versão 8.0.33

Usado para conectar a aplicação Spring Boot ao banco de dados MySQL.
Build:
Plugin Maven do Spring Boot: gerenciado pelo Spring Boot 3.3.4
O plugin spring-boot-maven-plugin facilita a construção e execução do projeto Spring Boot.
Observações:
As versões das dependências Spring (JPA, Security, etc.) são automaticamente gerenciadas pela versão 3.3.4 do Spring Boot.
Você está usando o MySQL 8.0.33 como banco de dados, integrado via o mysql-connector-java.

---- ---- ---- ----
---- Iniciar projeto

Aqui está como você pode iniciar seu projeto Spring Boot em três IDEs populares: Eclipse.

1. Eclipse:
Importe o projeto:

Vá em File > Import.
Selecione Existing Maven Projects e clique em Next.
Navegue até o diretório do seu projeto e clique em Finish.
Execute o projeto:

No Package Explorer, clique com o botão direito no arquivo principal da aplicação, geralmente o arquivo com a anotação @SpringBootApplication.
Selecione Run As > Spring Boot App ou Run As > Java Application.

Para acessar a página de login, acesse:
http://localhost:8080/login

---- ----

Acesse o link -> Sing up <- para cadastrar o usuário de acesso.

Para criar um usuário com permissão de administrador, digite o login como: admin

Para criar um usuário com permissão de bibliotecario, digite o login como: biblio

Para criar um usuário padrão, digite qualquer login sem ser um dos dois anteriores.

OBS: A senha pode ser qualquer uma para qualquer usuário.
