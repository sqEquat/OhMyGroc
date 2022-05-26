# OhMyGroc

## About

Этот pet-проект начинался как учебный по Spring'у и как инструмент для удобного семейного списка покупок  
Пока что готов только базовый функционал в виде rest api, так что его можно использовать с собственным фронтом

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
В теле запроса поля: ``username`` и ``password``  
При успешной аутентификации вы получите JWT токен:  
``{"access_token": TOKEN}``  
Его нужно использовать в хедере при каждом запросе, требующем авторизации  
``"Authorization": "Bearer TOKEN"``  

### Client Endpoints  
После аутентификации можно получить информацию о себе  
``GET /api/v2/client``  
Ответ содержит следующие данные о пользователе:  
1. Уникальный id пользователя
2. Email
3. Имя пользователя
4. Роли пользователя
4. Списки покупок

Каждый список состоит из:
1. Уникального id списка
2. Даты и времени создания
3. Списка предметов
 
Пример ответа:  
```
{  
    "id": 2,  
    "email": "client@mail.com",  
    "username": "client",  
    "roles": [  
        {  
            "id": 2,  
            "name": "ROLE_CLIENT"  
        }  
    ],  
    "shopLists": [  
        {  
            "id": 1,  
            "dateCreated": "2022-05-04T21:50:41.665535",  
            "items": [  
                "Milk",  
                "Bacon"  
            ]  
        },  
        {  
            "id": 2,  
            "dateCreated": "2022-05-04T21:52:03.822201",  
            "items": [  
                "Cheeseburger",  
                "Small fries",  
                "Cola"  
            ]  
        }  
    ]  
}  
```  

Получить все списки покупок  
``GET /api/v2/client/lists``  
Пример ответа:  
```
[  
    {  
        "id": 1,  
        "dateCreated": "2022-05-04T21:50:41.665535",  
        "items": [  
            "Milk",  
            "Bacon"  
        ]  
    },  
    {  
        "id": 2,  
        "dateCreated": "2022-05-04T21:52:03.822201",  
        "items": [  
            "Cheeseburger",  
            "Small fries",  
            "Cola"  
        ]  
    }  
]  
```

Получить список по его id  
``GET /api/v2/client/lists/{id}``  
Пример ответа:  
```
{  
    "id": 1,  
    "dateCreated": "2022-05-04T21:19:14.389671",  
    "items": [  
        "Bread",  
        "Milk",  
        "Sausages"  
    ]  
}  
```

Создать новый список  
``POST api/v2/client/lists``  
В теле запроса JSON:  
``{"items": ["Water", "Butter", "Bread"]}``  
Ответом будет созданный список:  
```
{  
    "id": 2,  
    "dateCreated": "2022-05-04T21:25:30.955745",  
    "items": [  
        "Water",  
        "Butter",  
        "Bread"  
    ]  
}  
```

Изменить содержимое списка  
``PUT api/v2/client/lists/{listId}``  
В теле запроса JSON:  
``{"items": ["Milk", "Bacon"]}``  
В ответе будет содержаться измененный список:
```
{  
    "id": 2,  
    "dateCreated": "2022-05-04T21:25:30.955745",  
    "items": [  
        "Milk",  
        "Bacon"  
    ]  
}  
```

Удалить список по его id  
``DELETE /api/v2/client/lists/{listId}``    
В случае успешного удаления, ответ будет содержать подобное сообщение:  
``Shopping list with id: {id} was deleted``

### Admin Endpoints
Если пользователь имеет роль администратора, то ему доступны дополнительные возможности.  
Получить список всех пользователей  
``GET /api/v2/admin/clients``  
Пример ответа:  
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

Получить список всех доступных для пользователей ролей  
``GET /api/v2/admin/roles``  
Пример ответа:  
```
[  
    {  
        "id": 1,  
        "name": "ROLE_ADMIN"  
    },  
    {  
        "id": 2,  
        "name": "ROLE_CLIENT"  
    },  
    {  
        "id": 3,  
        "name": "ROLE_GUEST"  
    }  
]  
```

Создать роль  
``POST /api/v2/admin/roles``  
В теле запроса JSON:  
``{"name": "ROLE_GUEST"}``  
Пример ответа:  
``{"id": 3, "name": "ROLE_GUEST"}``  

Удалить роль по id  
``DELETE /api/v2/roles/{roleId}``  
При успешном удаление результатом будет ответ:  
``Role deleted``

Добавить роль пользователю  
``PUT /api/v2/clients/{clientId}/roles/add/{roleId}``  
Ответ будет содержать измененного пользователя:  
```
{  
    "id": 2,  
    "email": "client@mail.com",  
    "username": "client",  
    "roles": [  
        {  
            "id": 2,  
            "name": "ROLE_CLIENT"  
        },  
        {  
            "id": 3,  
            "name": "ROLE_GUEST"  
        }  
    ],  
    "shopLists": []  
}  
```

Удалить роль у пользователя  
``PUT /api/v2/clients/{clientId}/roles/remove/{roleId}``  
Ответ будет содержать измененного пользователя:  
```
{  
    "id": 2,  
    "email": "client@mail.com",  
    "username": "client",  
    "roles": [  
        {  
            "id": 2,  
            "name": "ROLE_CLIENT"  
        }  
    ],  
    "shopLists": []  
}  
```

## Настройки
В файле application.properties содержатся параметры для работы приложения  
Информация для подключения к Базе Данных:  
1. URL
2. Username
3. Password

Настройки Hibernate:  
1. Режим Data Definition Language (ddl)
2. Режим отображения SQL запросов в консоли
3. Диалект базы данных
4. Режим форматирования SQL запросов

Также, для удобства разработки, здесь находятся некоторые настройки JWT:
1. Секрет для подписи токена
2. Время действительности токена в минутах

Во время запуска приложения в базу данных добавляется две роли:
1. ``ROLE_ADMIN``
2. ``ROLE_CLIENT``

А также два пользователя:  
1. ``username: admin, password: admin, roles: ROLE_ADMIN, ROLE_CLIENT``  
2. ``username: client, password: password, roles: ROLE_CLIENT``