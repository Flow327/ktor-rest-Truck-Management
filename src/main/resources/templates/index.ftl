<#import "template.ftl" as layout />
<@layout.mainLayout>
    <a href="/driver?action=new" class="btn btn-primary float-right mb-1" role="button">New Driver</a>
    <style>
        td {
            width: 0;
        }

        .btn {
            width: 100px;
            text-align: center;
        }
    </style>
    <a href="/weather" class="btn btn-success float-left mb-1" role="button">Weather</a>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Company Name</th>
            <th scope="col">Parking Number</th>
            <th scope="col">Truck Number</th>
            <th scope="col">Contents</th>
            <th scope="col">Container</th>
            <th scope="col">Comments</th>
            <th scope="col">Time Stamp</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list drivers as drv>
            <tr>
                <td>${drv.id}</td>
                <td>${drv.name}</td>
                <td>${drv.parking}</td>
                <td>${drv.truckNumber}</td>
                <td>${drv.contents}</td>
                <td>${drv.container}</td>
                <td>${drv.comments}</td>
                <td>${drv.timeStamp}</td>
                <td>
                    <a href="/driver?action=edit&id=${drv.id}" class="btn btn-secondary" role="button">Edit</a>
                    <a href="/delete?id=${drv.id}" class="btn btn-danger" role="button">Del</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@layout.mainLayout>