<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<h2 class=space-below>REST Tool</h2>

<div class=block5>
    <h3>Options</h3>
    <div class=rest-resources>
        <h4>Controller (resource)</h4>
        <div class=rest-options>
            <g:each in="${restList}">
               <label><input type=radio name=rest-resource value=${it.key}>${it.key}</label>
            </g:each>
        </div>
    </div>
    <div class=rest-actions>
        <h4>Action (method)</h4>
        <g:each in="${restList}">
            <div id=rest-actions-${it.key} class=rest-options>
                <label><input type=radio name=rest-action-${it.key} value=index checked>[none]</label>
                <g:each var="action" in="${it.value}">
                    <label><input type=radio name=rest-action-${it.key} value=${action}>${action}</label>
                </g:each>
            </div>
        </g:each>
    </div>
    <div class=rest-extras>
        <p>
            <label>Resource ID:<input id=rest-id type=number min=1 size=5></label>
            <label>Parameters: ?<input id=rest-params type=text size=20></label> example: <i>brand=381&amp;max=5</i>
        </p>
        <p id=rest-url></p>
        <p><button id=rest-submit>Submit REST Request</button></p>
    </div>
</div>

<div class=block6>
    <h3>Responses</h3>
    <div class=rest-responses>
        <div class=dna-template data-dna-name=rest-response>
            <div class=responses-url><code>~~url~~</code></div>
            <pre><code class=language-json>~~json~~</code></pre>
        </div>
    </div>
</div>
