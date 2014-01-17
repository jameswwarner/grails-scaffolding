<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<h2 class=space-below>Site Configuration</h2>
<div class=block1>
    <h3>Configuration Items</h3>
    <div id=site-cfg>
        <table class=data-box>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Code</th>
                    <th>Description</th>
                    <th>Value</th>
                    <th>Mktg Hub</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                <tr id=site-cfg-row-stub class=hide-me>
                    <td class=site-cfg-id></td>
                    <td class=site-cfg-code><input type=text class=field-medium></td>
                    <td class=site-cfg-name><input type=text class=field-xxlarge></td>
                    <td class=site-cfg-value><input type=text class=field-large></td>
                    <td class=site-cfg-editable><input type=checkbox>Editable</td>
                    <td><button class=site-cfg-action-delete>&times;</button></td>
                </tr>
            </tbody>
        </table>
        <p class=align-right>
            <button id=site-cfg-action-add>+ New Configuration Item</button>
        </p>
    </div>
</div>
