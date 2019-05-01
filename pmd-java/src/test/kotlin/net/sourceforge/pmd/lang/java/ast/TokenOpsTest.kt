/*
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast

import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.FunSpec
import net.sourceforge.pmd.lang.ast.GenericToken
import net.sourceforge.pmd.lang.ast.test.Assertions
import net.sourceforge.pmd.lang.ast.test.containingFile
import net.sourceforge.pmd.lang.ast.test.firstToken

/**
 * @author Clément Fournier
 */
class TokenOpsTest : FunSpec({

    fun setup1(assertions: Assertions<List<GenericToken>>) {
        with(ParserTestCtx(JavaVersion.J11)) {


            val decl = parseToplevelAnyTypeDeclaration("""
                class Foo { /* wassup */ abstract void bar(); }
            """.trimIndent())

            val fileTokens = generateSequence(decl.containingFile.firstToken) { it.next }.toList()

            fileTokens.map { it.image } shouldBe listOf(
                    // for some reason there's 2 EOF tokens but that's not the point of this test
                    "class", "Foo", "{", "abstract", "void", "bar", "(", ")", ";", "}", "", ""
            )

            assertions(fileTokens)

        }
    }


    test("Test nth previous token, simple cases") {

        setup1 { fileTokens ->

            val absToken = fileTokens[3].also { it.image shouldBe "abstract" }

            TokenOps.nthPrevious(fileTokens[0], absToken, 1).image shouldBe "{"
            TokenOps.nthPrevious(fileTokens[0], absToken, 2).image shouldBe "Foo"
            TokenOps.nthPrevious(fileTokens[0], absToken, 3).image shouldBe "class"
        }
    }

    test("Test nth previous token, wrong left hint") {

        setup1 { fileTokens ->
            // hint is after
            shouldThrow<IllegalStateException> {
                TokenOps.nthPrevious(fileTokens[4], fileTokens[3], 1)
            }

            // same hint
            shouldThrow<IllegalStateException> {
                TokenOps.nthPrevious(fileTokens[3], fileTokens[3], 2)
            }
        }
    }

    test("Test nth previous token, wants to go too far left") {

        setup1 { fileTokens ->
            shouldThrow<NoSuchElementException> {
                TokenOps.nthPrevious(fileTokens[0], fileTokens[3], 4)
            }
        }
    }


})
