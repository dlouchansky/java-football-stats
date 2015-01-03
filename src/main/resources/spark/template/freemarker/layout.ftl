<#macro layout title>
    <html>
    <head>
        <title>${title}</title>
        <link rel="stylesheet" href="/kube.min.css"/>
        <link rel="stylesheet" href="/style.css"/>
    </head>
    <body>
    <div class="content">
        <div class="units-row">
            <div class="unit-20">
                <nav class="nav nav-stacked">
                    <ul>
                        <li><strong><a href="/">MPT PD2</a></strong></li>
                        <li><a href="/upload">Upload</a></li>
                        <li><a href="/top">Team top</a></li>
                        <li><a href="/topPlayers">Player top</a></li>
                        <li><a href="/topGoalkeepers">Goalkeeper top</a></li>
                        <li><a href="/referees">Referee top</a></li>
                        <li><a href="/rude">Rude player top</a></li>
                    </ul>
                </nav>
            </div>
            <div class="unit-80">
                <div>
                    <h2>${title}</h2>
                    <#nested>
                </div>
            </div>
        </div>
    </div>

    </body>
    </html>
</#macro>