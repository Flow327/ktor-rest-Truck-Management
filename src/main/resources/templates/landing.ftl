<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Walgreens Driver Page</title>
    <!-- Add any required CSS and JavaScript files here -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
          crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>Walgreens</h1>
    <form action="/submit" method="post">
        <input type="hidden" name="doorOrLane" value="${doorOrLane}">
        <div class="form-group">
            <label for="companyName">Company Name:</label>
            <input type="text" class="form-control" id="companyName" name="companyName" required>
        </div>
        <div class="form-group">
            <label for="trailerNumber">Trailer Number:</label>
            <input type="text" class="form-control" id="trailerNumber" name="trailerNumber" required>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>
</body>
</html>