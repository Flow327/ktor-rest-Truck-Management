<#import "template.ftl" as layout />
    <!doctype html>
    <html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Yard Lane</title>
    <style>
        /* Add your styles here */
        .occupied {
            background-color: red;
        }
        table {
            border-collapse: collapse;
        }

        body {
            margin: 50px;
        }

        td {
            vertical-align: bottom;
            text-align: left;
        }

        td {
            border: 1px solid black;
            padding: 8px;
            text-align: center;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr {
            margin-bottom: 10px;
        }
    </style>
</head>
    <body>
<@layout.mainLayout>
    <table>
        <#assign totalSpots = 314 - 216 + 1>
        <#assign spotsPerRow = 7>
        <#assign numRows = totalSpots / spotsPerRow>
        <#list 0..numRows as row>
            <tr>
                <#list 0..(spotsPerRow - 1) as col>
                    <#assign lotNumber = 216 + (row * spotsPerRow) + col>
                    <#if lotNumber <= 314>
                        <#assign isUsed = false>
                        <#assign truckNumber = "">
                        <#list drivers as driver>
                            <#if driver.parking == lotNumber>
                                <#assign isUsed = true>
                                <#assign truckNumber = driver.truckNumber>
                            </#if>
                        </#list>
                        <td <#if isUsed>class="occupied"</#if>>
                            Yard Lane: ${lotNumber} <br> <#if isUsed>Truck Number: ${truckNumber}</#if>
                        </td>
                    </#if>
                </#list>
            </tr>
            <tr style="height: 15px;"></tr>
        </#list>
    </table>
</body>
</html>
</@layout.mainLayout>