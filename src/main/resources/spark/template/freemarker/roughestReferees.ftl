<#import "layout.ftl" as u>

    <@u.layout title="Referee top by given cards">
    <table>
        <thead>
        <tr>
            <td>Nr.</td>
            <td>Name</td>
            <td>Cards average for game</td>
            <td>Games</td>
            <td>Cards</td>
        </tr>
        <thead>
        <tbody>
        <#list results as result>
            <tr>
                <td><span class=" badge
                <#if result.nr==1>
                badge-red
                <#elseif result.nr==2>
                badge-yellow
                <#elseif result.nr==3>
                badge-green
                </#if>
                ">${result.nr}</span></td>
                <td>${result.name}</td>
                <td>${result.cardGameAverage?string["0.#"]}</td>
                <td>${result.games}</td>
                <td>${result.cards}</td>
            </tr>
        </#list>
        </tbody>
    </table>

</@u.layout>
