<!DOCTYPE html>
<html lang="en">
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <title>Weather App</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }
        h1 {
            font-size: 36px;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
            margin-bottom: 10px;
        }
    </style>
</head>
<#if weatherResponse.weather[0].description == "Clear">
<body style="background-image: url('clear-sky.jpg');">
<#elseif weatherResponse.weather[0].description == "Clouds">
<body style="background-image: url('cloudy-sky.jpg');">
<#elseif weatherResponse.weather[0].description == "few clouds">
<body style="background-image: url('few-clouds.jpg')"
<#else>
<body>
</#if>
<div class="container">
    <h1>Windsor Weather</h1>
    <p>Temperature: ${weatherResponse.main.temp?round} &#8457; <img src="http://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}.png" alt="${weatherResponse.weather[0].description}"></p>
    <p>Feels like: ${weatherResponse.main.feels_like?round} &#8457;</p>
    <p>Humidity: ${weatherResponse.main.humidity}%</p>
    <p>Weather: ${weatherResponse.weather[0].main}</p>
    <p>Description: ${weatherResponse.weather[0].description}</p>
    <p>Wind speed: ${weatherResponse.wind.speed?round} Mph </p>
    <p>Wind direction: ${weatherResponse.wind.deg} degrees</p>
    <p>Cloudiness: ${weatherResponse.clouds.all}%</p>
</div>

</body>

</html>