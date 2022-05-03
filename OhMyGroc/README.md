# OhMyGroc

## About

Этот pet-проект начинался как учебный по Spring'у и как инструмент для удобного семейного списка покупок  
Пока что готов только базовый функционал в виде rest api

## API

### Authentication/Authorization and Registration

Для регистрации клиента необходимо послать POST запрос на "/api/v2/registration"  
В теле запроса JSON с данным для регистрации:  
```
{  
    "email": "test@mail.com",  
    "username": "client",  
    "password": "password"  
}
```

Для аутентификации необходимо послать POST запрос на "/api/v2/login"  
В теле поля: ``username`` и ``password``  
При успешной аутентификации вы получите JWT токен, его нужно добавить в хедер при каждом запросе  
``"Authorization": "Bearer TOKEN"``  

### Client Endpoints  
После аутентификации можно получить инофрмацию о себе с помощью GET запроса на "/api/v2/client":  
1. Уникальный id пользователя
2. Email
3. Имя пользователя
4. Списки покупок 
 
Пример:  
```
{  
    "id": 2,  
    "email": "client@mail.com",  
    "username": "client",  
    "shopLists": []  
} 
```  

Также можно получить все списки покупок GET запросом на "/api/v2/client/lists"  
Список покупок содержит:  
1. Уникальный id списка
2. Дату и время создания
3. Список предметов

Конкретный список можно получить с помощью его id GET на "/api/v2/client/lists/{id}"

Для того чтобы создать новый список необходимо выполнить POST запрос на "api/v2/client/lists"  
В теле запроса JSON:
``{"items": ["Water", "Butter", "Bread"]}``  

Изменить содержимое списка можно PUT запросом на "api/v2/client/lists/{listId}"  
В теле запроса JSON: ``{"items": ["Milk", "Becon"]}``  

Удалить список по его id можно DELETE запросом на "/api/v2/client/lists/{listId}"

### Admin Endpoints
Если пользователь имеет роль администратора, то ему доступны дополнительные возможности  
Можно получить список всех пользователей с помощью GET запроса на "/api/v2/admin/clients"  
Результат:  
```
[  
    {  
        "id": 1,  
        "email": "admin@mail.com",  
        "username": "admin",  
        "shopLists": []  
    },  
    {  
        "id": 2,  
        "email": "client@mail.com",  
        "username": "client",  
        "shopLists": []  
    }  
]  
```

Создать роль POST запросом на "/api/v2/admin/roles"  
В теле запроса JSON: ``{"name": "ROLE_GUEST"}``  
Ответ содержит JSON с id и именем созданной роли: ``{"id": 3, "name": "ROLE_GUEST"}``  

Удалить роль по id с помощью DELETE запроса на "/api/v2/roles/{roleId}"  
При успешном удаление результатом будет ответ: ``Role deleted``