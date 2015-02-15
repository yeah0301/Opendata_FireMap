<?php

?>

<style>
div{
        display: block;
    }    
    
.simple table{
        width: 66.666%!important;
        min-width: 1px;
    }
    
.line{
        margin-top: 0!important;
        
    }
hr{
        border: 0;
        border-bottom: 1px solid #e9e9e9;
        
    }
    
.space{
        margin-top: 27px!important;
    }
</style>


<div class="simple table" >

    <div class="space"></div>

    <div class="table title">
        <h3>
            <b id="table_name"></b>
        </h3>
    </div>

    <hr class="line">

    <table class="table table-hover" id="table" >
        <thead>
            <tr>
                <th>#</th>
                <th>名稱</th>
                <th>緯度</th>
                <th>經度</th>
            </tr>
        </thead>
            <tbody id="test"></tbody>
    </table>

</div>