<#macro mainLayout title="Welcome to Yard Management Database">
    <!doctype html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>${title}</title>
        <!-- Vanilla Datepicker CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.3.1/dist/css/datepicker.min.css">

        <!-- Vanilla Datepicker JS -->
        <script src="https://cdn.jsdelivr.net/npm/vanillajs-datepicker@1.3.1/dist/js/datepicker-full.min.js"></script>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/css/bootstrap.min.css" integrity="sha384-DhY6onE6f3zzKbjUPRc2hOzGAdEf4/Dz+WJwBvEYL/lkkIsI3ihufq9hk9K4lVoK" crossorigin="anonymous">

        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.4.min.js" integrity="sha256-oP6HI9z1XaZNBrJURtCoUT5SUnxFr8s3BzRl+cbzUq8=" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.6.4.js" integrity="sha256-a9jBBRygX1Bh5lt8GZjXDzyOB+bWve9EiO7tROUtj/E=" crossorigin="anonymous"></script>

        <!-- Popper JS -->
        <script src="https://unpkg.com/@popperjs/core@2"></script>

        <!-- Bootstrap JavaScript -->
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/5.0.0-alpha2/js/bootstrap.bundle.min.js" integrity="sha384-BOsAfwzjNJHrJ8cZidOg56tcQWfp6y72vEJ8xQ9w6Quywb24iOsW913URv1IS4GD" crossorigin="anonymous"></script>

        <!-- Bundled Bootstrap -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM" crossorigin="anonymous"></script>

    </head>
        <style>
            #search-form {
                position: absolute;
                right: 0;
            }
        </style>
        <style>
            .custom-container {
                max-width: 1250px;

            }
        </style>
        <style>
            .search-button {
                height: 38px;
                border-color: #000000;
                color: #000000;
            }
        </style>
    <style>
        #inline-datepicker {
            width: auto !important;
            display: block !important;
            margin: 0 auto !important;

        }
        .date-with-record {
            background-color: lightblue;
        }

    </style>
    <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Yard Management</a>
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
                    <li class="nav-item">
                        <a id="calendar" class="nav-link active" data-bs-target="#calendarModal" aria-expanded="false">Calendar</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/parkingLot">Parking Lot</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/doorNumber">Door Numbers</a>
                    </li>
                    <div class="modal fade" id="calendarModal" tabindex="-1" aria-labelledby="calendarModalLabel" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered modal-sm">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5 text-center" id="calendarModalLabel">Yard out Calendar</h1>
                                </div>
                                <div class="modal-body">
                                    <div id="inline-datepicker">
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <a class="btn btn-secondary" role="button" href="/yardout">Yard Out Table</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    </li>
                </ul>
                <form id="search-form" class="d-flex " action=/search method="post">
                    <label>
                        <input class="form-control me-sm-2" type="search" name="query" placeholder="Search">
                    </label>
                    <button class="btn btn-outline-success my-2 my-sm-0 search-button" type="submit">Search</button>
                </form>
            </div>
        </div>
    </nav>
    <script>
        async function fetchDatesWithRecords() {
            const response = await fetch('/dates-with-records');
            const data = await response.json();
            return data.map(dateString => new Date(dateString));
        }

        fetchDatesWithRecords().then(dates => {
            datesWithRecords = dates;

            new Datepicker(elem, {
                // options here
                inline: true,
                beforeShowDay: (date) => {
                    const hasRecord = datesWithRecords.some(
                        recordDate => recordDate.toISOString().split('T')[0] === date.toISOString().split('T')[0]
                    );

                    return { enabled: true, classes: hasRecord ? 'date-with-record' : '' };
                }
            });
        });

        (async function setupCalendar() {
            const datesWithRecords = await fetchDatesWithRecords();

            const elem = document.querySelector('#inline-datepicker');
            new Datepicker(elem, {
                // options here
                inline: true,
                beforeShowDay: (date) => {
                    const hasRecord = datesWithRecords.some(
                        recordDate => recordDate.toISOString().split('T')[0] === date.toISOString().split('T')[0]
                    );

                    return { enabled: true, classes: hasRecord ? 'date-with-record' : '' };
                }
            });


            elem.addEventListener('changeDate', function (e) {
                const selectedDate = e.detail.date;
                const formattedDate = selectedDate.toISOString().split('T')[0];
                window.location.href = '/day?date=' + formattedDate;
            });
        })();

        var calendarModal = document.getElementById('calendarModal');
        var myButton = document.querySelector('[data-bs-target="#calendarModal"]');
        myButton.addEventListener('click', function () {
            var modal = new bootstrap.Modal(calendarModal);
            modal.show();
        });
    </script>

    </body>
    <div class="container custom-container">
        <div class="row m-1">
            <div class="row m-1">
                <#nested/>
            </div>
        </div>
    </div>
    </html>
</#macro>


