<#macro mainLayout title="Welcome to Yard Management Database">
    <!doctype html>
    <html lang="en" xmlns="http://www.w3.org/1999/html">
    <head>
        <title>${title}</title>
        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <!--bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
              integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
              crossorigin="anonymous">
    <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Yard Management</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarColor01">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="/driver?action=new">New Driver</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/stats">Status</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/weather">Weather</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="/" role="button" aria-haspopup="true" aria-expanded="false">Dropdown</a>
                        <div class="dropdown-menu">
                            <a class="dropdown-item" href="/">Action</a>
                            <a class="dropdown-item" href="/">Another action</a>
                            <a class="dropdown-item" href="/">Something else here</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="/">Separated link</a>
                        </div>
                    </li>
                </ul>
                <form id="search-form" class="d-flex" action=/search method="post">
                    <label>
                        <input class="form-control me-sm-2" type="search" name="query" placeholder="Search">
                    </label>
                    <button class="btn btn-secondary my-2 my-sm-0" type="submit">Search</button>
                </form>
            </div>
        </div>
    </nav>
    </body>
    <div class="container">
        <div class="row m-1">
            <div class="row m-1">
                <#nested/>
            </div>
        </div>
    </div>
    </html>
</#macro>