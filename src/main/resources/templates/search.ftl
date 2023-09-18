<#import "template.ftl" as layout />
<@layout.mainLayout>
    <style>
        td {
            width: 0;
        }

        .btn {
            width: 100px;
            text-align: center;
        }
    </style>
    <table class="table">
        <thead class="thead-dark">
        <tr>
            <th scope="col">Id</th>
            <th scope="col">Door Or Lane Number</th>
            <th scope="col">Trailer Number</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list drivers as drv>
            <tr>
                <td>${drv.id}</td>
                <td>${drv.parking}</td>
                <td>${drv.truckNumber}</td>
                <td>
                    <a href="/driver?action=edit&id=${drv.id}" class="btn btn-secondary" role="button">Edit</a>
                    <a href="/delete?id=${drv.id}" class="btn btn-danger" role="button">Del</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

</@layout.mainLayout>