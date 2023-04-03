<#import "template.ftl" as layout />
<@layout.mainLayout>
    <style>
        .btn {
            width: 100px;
            text-align: center;
        }
        .table-hover {
            width: 100% !important;
            height: max-content !important;
        }

        th, td {
            /* center the content of all table cells */
            vertical-align: middle; !important;/* vertically center the content of all table cells */
            padding: 10px !important;
            text-align: center !important;
        }
        .thead-light th {
            height: 15px !important; /* adjust this value to set the desired height of the table header row */
            padding: 0; /* remove the default padding from the table header cells */
        }
    </style>
    <table class="table table-hover">
        <caption style="caption-side: top; font-weight: bold;">YARD OUT TABLE</caption>
        <thead>
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Company Name</th>
            <th scope="col">Door Or Lane Number</th>
            <th scope="col">Trailer Number</th>
            <th scope="col">Contents</th>
            <th scope="col">Container</th>
            <th scope="col">Comments</th>
            <th scope="col">Time Stamp</th>
            <th scope="col">Update Stamp</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list yardOutDrivers as drv>
            <tr>
                <td>${drv.id}</td>
                <td>${drv.name}</td>
                <td>${drv.parking}</td>
                <td>${drv.truckNumber}</td>
                <td>${drv.contents}</td>
                <td>${drv.container}</td>
                <td>${drv.comments}</td>
                <td>${drv.timeStamp}</td>
                <td>${drv.updateStamp}</td>
                <td>
                    <a href="/return?id=${drv.id}" type="button" class="btn btn-primary">Return</a>
                    <a href="/deleteyard?action=delete&id=${drv.id}" class="btn btn-danger" role="button">Del</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
    </html>
</@layout.mainLayout>
