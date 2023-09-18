<#import "template.ftl" as layout />
<@layout.mainLayout title="New Driver">
    <form action="/driver" method="post" xmlns="http://www.w3.org/1999/html">
        <div class="form-group">
            <label for="parking">Parking Or Door</label>
            <select type="text" class="form-control" id="parking" name="parking">
                <option value="" disabled selected> Current Number ${(driver.parking)!}</option>
                <optgroup label="Parking Numbers">
                    <#list parkingNumbers as number>
                        <option value="${number}" <#if (driver.parking)!?string != "" && (driver.parking)!?number == number>selected</#if>>Parking Number ${number}</option>
                    </#list>
                </optgroup>
                <optgroup label="Door Numbers">
                    <#list doorNumbers as number>
                        <option value="${number}" <#if (driver.door)!?string != "" && (driver.door)!?number == number>selected</#if>>Door Number ${number}</option>
                    </#list>
                </optgroup>
            </select>
        </div>
        <div class="form-group">
            <label for="truckNumber">Trailer Number</label>
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
