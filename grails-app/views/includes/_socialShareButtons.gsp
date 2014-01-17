<%-- Copyright (c) 2013-2014 James W. Warner, All Rights Reserved --%>
<div class=social-share>
    <div class=fb-like data-href="${shareUrl}" data-send=false
        data-layout=button_count data-width=200 data-show-faces=false>
    </div>
    <div class=social-share-twitter>
       <a href="//twitter.com/share" class=twitter-share-button data-url="${shareUrl}" data-text="${shareText}" data-count=none>Tweet</a>
    </div>
    <div class=social-share-pinterest>
        <a href="//pinterest.com/pin/create/button/?url=${shareUrl.encodeAsURL()}&media=http:${shareImg.encodeAsURL()}&description=${shareText.encodeAsURL()}" class="pin-it-button" count-layout="horizontal"><img src="//assets.pinterest.com/images/PinExt.png" alt="Pin It"></a>
    </div>
</div>
