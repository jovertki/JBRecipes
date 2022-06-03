# Recipes

## Описание
Вебсервис для хранения рецептов на Spring Boot, Spring Data

В качестве базы данных используется H2

### Входные точки:

#### POST /api/recipe/new

Принимает на вход JSON рецепта в формате
```
{
    "name": "recipe name",
    "description": "recipe description",
    "category": "recipe category",
    "directions": ["direction 1", "direction 2", ...],
    "ingredients": ["ingredient 1", "ingredient 2", .,.]
}
```
и возвращает 
```
{
    "id": "integer id"
}
```
#### GET /api/recipe/{id}

Возвращает в формате JSON рецепт по id с полем "date" с датой последнего обновления

#### DELETE /api/recipe/{id}

Удаляет рецепт по id из базы данных

#### PUT /api/recipe/{id}

Принимает на вход JSON и обновляет рецепт по id

#### GET /api/recipe/search

При передаче параметра name возвращает все рецепты, которые содержат в названии значение параметра, case insensitive

При передаче параметра category возвращает все рецепты переданной категории
