<#import "template.ftl" as layout />
<@layout.mainLayout title="New Driver">
    <form action="/driver" method="post">
        <div class="form-group">
            <label for="name">Name</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Enter Company Name"
                   value="${(driver.name)!}">
        </div>
        <div class="form-group">
            <label for="parking">Parking</label>
            <input type="text" class="form-control" id="parking" name="parking" placeholder="Enter Parking Number"
                   value="${(driver.parking)!}">
        </div>
        <div class="form-group">
            <label for="truckNumber">Truck Number</label>
            <input type="text" class="form-control" id="truckNumber" name="truckNumber" placeholder="Enter Truck Number"
                   value="${(driver.truckNumber)!}">
        </div>
        <div class="form-group">
            <label for="contents">Contents</label>
            <input type="text" class="form-control" id="contents" name="contents" placeholder="Enter Contents"
                   value="${(driver.contents)!}">
        </div>
        <div class="form-group">
            <label for="container">Container</label>
            <input type="text" class="form-control" id="container" name="container" placeholder="Empty Or Not"
                   value="${(driver.container)!}">
        </div>
        <div class="form-group">
            <label for="comments">Comment</label>
            <input type="text" class="form-control" id="comments" name="comments" placeholder="Enter Comment"
                   value="${(driver.comments)!}">
        </div>
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="id" name="id" value="${(driver.id)!}">
        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</@layout.mainLayout>
