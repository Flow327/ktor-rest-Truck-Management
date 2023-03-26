<#import "template.ftl" as layout />
<@layout.mainLayout>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <title>Weather App</title>
        <style>
            .card {
                background-color: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
            }
        </style>
    </head>
    <#if weatherResponse.weather[0].description == "Clear">
    <body style="background-image: url('/static/clear-sky.png');">
    <#elseif weatherResponse.weather[0].description == "overcast clouds">
    <body style="background-image: url('static/cloudy-sky.png');">
    <#elseif weatherResponse.weather[0].description == "few clouds">
        <body style="background-image: url('/static/few-clouds.png')"
    <#else>
    <body>
    </#if>
    <div class="container">
        <div class="card text-black bg-success">
            <h2>Current Weather</h2>
            <p>Temperature: ${weatherResponse.main.temp?round} &#8457; <img src="http://openweathermap.org/img/wn/${weatherResponse.weather[0].icon}.png" alt="${weatherResponse.weather[0].description}"></p>
            <p>Minimum Temperature: ${weatherResponse.main.temp_min?round} &#8457;</p>
            <p>Maximum Temperature: ${weatherResponse.main.temp_max?round} &#8457;</p>
            <p>Feels like: ${weatherResponse.main.feels_like?round} &#8457;</p>
            <p>Humidity: ${weatherResponse.main.humidity}%</p>
            <p>Weather: ${weatherResponse.weather[0].main}</p>
            <p>Description: ${weatherResponse.weather[0].description}</p>
            <p>Wind speed: ${weatherResponse.wind.speed?round} Mph </p>
            <p>Wind direction: ${weatherResponse.wind.deg} degrees</p>
            <p>Cloudiness: ${weatherResponse.clouds.all}%</p>
        </div>
    </div>
    </body>
</@layout.mainLayout>