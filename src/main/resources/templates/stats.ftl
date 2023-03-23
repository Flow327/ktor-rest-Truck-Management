<#import "template.ftl" as layout />
<@layout.mainLayout>
    <!DOCTYPE html>
    <html lang="en">
<head>
    <title>Trailer Status</title>
</head>
    <table class="table table-hover">
    <thead>
    <tr>
        <th scope="row">Full Trailers</th>
        <th scope="row">Empty Trailers</th>
        <th scope="row">Full Containers</th>
        <th scope="row">Empty Containers</th>
        <th scope="row">Full Chep Trailers</th>
        <th scope="row">Full Blonde Trailers</th>
        <th scope="row">Number of Trailers</th>
        <th scope="row">Occupied</th>
    </tr>
    </thead>
    <tbody>
    <#list stats as st>
        <tr>
            <td>${st.fullTrailers}</td>
            <td>${st.emptyTrailers}</td>
            <td>${st.fullContainers}</td>
            <td>${st.fullChep}</td>
            <td>${st.fullBlonde}</td>
            <td>${st.numberOfTrailers}</td>
            <td>${st.Occupied}</td>
            <td>
                <div class="progress">
                    <div class="progress-bar" role="progressbar" style="width: 75%" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100">
                        <span>75%</span>
                    </div>
                </div>
            </td>
        </tr>
    </#list>
    </tbody>
</table>
</html>
</@layout.mainLayout>