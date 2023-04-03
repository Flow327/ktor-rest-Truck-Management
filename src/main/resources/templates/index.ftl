<#import "template.ftl" as layout />
<@layout.mainLayout>
<!doctype html>
<html lang="en">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
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

        .btn-danger {
            width: 6rem;
        }

        .btn-secondary {
            width: 5rem;
        }

        .dropdown-toggle-split {
            width: 1rem;
        }
        .dropdown-menu {
            background-color: deepskyblue;
            white-space: nowrap;
            padding: 0;
            width: 50%;
        }
        .dropdown-item {
            border: 1px solid yellow;
            padding: 5px 5px;
            text-align: center;
        }
        .dropdown-item:hover {
            background-color: deepskyblue;
        }
    </style>
    <table class="table table-hover">
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
                <td>${drv.updateStamp}</td>
                <td>
                    <div class="btn-group">
                        <a href="/driver?action=edit&id=${drv.id}" class="btn btn-secondary" role="button">Edit</a>
                        <button type="button" class="btn btn-sm btn-secondary dropdown-toggle dropdown-toggle-split" data-bs-toggle="dropdown" aria-expanded="false">
                            <span class="visually-hidden">Toggle Dropdown</span>
                        </button>
                        <ul class="dropdown-menu">
                            <li>
                                <form action="/yardout?action=yardout" method="post">
                                    <input type="hidden" name="id" value="${drv.id}">
                                    <button type="submit" class="dropdown-item">Yard Out</button>
                                </form>
                            </li>
                        </ul>
                    </div>
                    <a href="/deleteDriver?action=delete&id=${drv.id}" class="btn btn-danger" role="button">Delete</a>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</html>
</@layout.mainLayout>


