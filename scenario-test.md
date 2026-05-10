# Scenario Test – Stock Trading Game

## Test Case 1 – Successful Buy Stock

Test ID: SC-BUY-01  
Use Case: Buy Stock  
Actor: User

### Preconditions
- Application is running
- User has 10000 kr balance
- Stock AAPL exists

| Step | User Action               | Expected System Response     |
|------|---------------------------|------------------------------|
| 1    | Open Stock Market screen  | Stock list is shown          |
| 2    | Select AAPL stock         | AAPL becomes selected        |
| 3    | Enter quantity = 1        | Input is accepted            |
| 4    | Click "Køb valgt"         | Stock is purchased           |
| 5    | Open Portfolio screen     | Owned stock is visible       |
| 6    | Check balance             | Balance is lower than before |
| 7    | Check transaction history | BUY transaction is shown     |


---

## Test Case 2 – Buy Without Enough Balance

Test ID: SC-BUY-02  
Use Case: Buy Stock  
Actor: User

### Preconditions
- Application is running
- User balance is 50 kr
- Stock AAPL costs more than 50 kr

| Step | User Action              | Expected System Response |
|------|--------------------------|--------------------------|
| 1    | Open Stock Market screen | Stock list is shown      |
| 2    | Select AAPL              | Stock becomes selected   |
| 3    | Enter quantity = 1       | Input is accepted        |
| 4    | Click "Køb valgt"        | Purchase fails           |
| 5    | Observe popup            | Error message is shown   |
| 6    | Check balance            | Balance is unchanged     |


---

## Test Case 3 – Successful Sell Stock

Test ID: SC-SELL-01  
Use Case: Sell Stock  
Actor: User

### Preconditions
- User owns 2 TSLA shares

| Step | User Action               | Expected System Response |
|------|---------------------------|--------------------------|
| 1    | Open Portfolio screen     | Owned stocks are shown   |
| 2    | Select TSLA               | TSLA becomes selected    |
| 3    | Enter quantity = 1        | Input is accepted        |
| 4    | Click "Sælg valgt"        | Sale succeeds            |
| 5    | Check owned shares        | Quantity decreases to 1  |
| 6    | Check balance             | Balance increases        |
| 7    | Check transaction history | SELL transaction appears |


---

## Test Case 4 – Sell Too Many Shares

Test ID: SC-SELL-02  
Use Case: Sell Stock  
Actor: User

### Preconditions
- User owns 2 TSLA shares

| Step | User Action (Act)     | Expected System Response (Assert) |
|------|-----------------------|-----------------------------------|
| 1    | Open Portfolio screen | Owned stocks are shown            |
| 2    | Select TSLA           | TSLA becomes selected             |
| 3    | Enter quantity = 3    | Input is accepted                 |
| 4    | Click "Sælg valgt"    | Sale fails                        |
| 5    | Observe popup         | Error message is shown            |
| 6    | Check owned shares    | Quantity remains 2                |