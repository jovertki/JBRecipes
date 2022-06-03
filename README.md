# Recipes

Вебсервис для хранения рецептов

## Использованные технологии
- Spring Boot
- Spring Data
- REST API
- CRUD
- H2
- Project Lombok

### JSON рецепта
```
{
    "name": "recipe name",
    "description": "recipe description",
    "category": "recipe category",
    "directions": ["direction 1", "direction 2", ...],
    "ingredients": ["ingredient 1", "ingredient 2", .,.]
}
```
### Входные точки:

```POST /api/recipe/new```      Добавляет описанный в JSON рецепт, возвращает id

```GET /api/recipe/{id}```      Возвращает в формате JSON рецепт по id с полем "date" с датой последнего обновления

```DELETE /api/recipe/{id}```   Удаляет рецепт по id из базы данных

```PUT /api/recipe/{id}```      Принимает на вход JSON и обновляет рецепт по id

```GET /api/recipe/search```       Возвращает рецепты, в зависимости от параметра:

* ```name=someName```           возвращает рецепты, которые содержат в названии значение параметра, case insensitive
* ```category=categoryName```   возвращает рецепты заданной категории case insensitive

## TODO
* Добавить авторизацию через Spring Security
