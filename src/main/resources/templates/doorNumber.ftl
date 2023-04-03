<#import "template.ftl" as layout />
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Door Numbers</title>
    <style>
        /* Add your styles here */
        .occupied {
            background-color: red;
        }
        .cardboard {
            background-color: orange;
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
    <#assign totaldoors = 98 - 50 + 1>
    <#assign doorsPerRow = 7>
    <#assign numRows = totaldoors / doorsPerRow>
    <#list 0..numRows as row>
        <tr>
            <#list 0..(doorsPerRow - 1) as col>
                <#assign doorNumber = 50 + (row * doorsPerRow) + col>
                <#if doorNumber <= 98>
                    <#assign isUsed = false>
                    <#assign door = "">
                    <#list drivers as driver>
                        <#if driver.parking == doorNumber>
                            <#assign isUsed = true>
                            <#assign door = driver.door>
                            <#assign truckNumber = driver.truckNumber>
                        </#if>
                    </#list>
                    <td <#if isUsed>class="occupied" <#elseif doorNumber == 74 || doorNumber == 75>class="cardboard"</#if>>
                        Door: ${doorNumber} <br>
                        <#if doorNumber == 74 || doorNumber == 75>Cardboard</#if> <!-- Add this line -->
                        <#if isUsed>Truck Number: ${truckNumber}</#if>
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
