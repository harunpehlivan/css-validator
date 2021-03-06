//
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM, Keio, Beihang, 2016.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.properties.css.CssProperty;
import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssIdent;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;
import org.w3c.css.values.CssValueList;

import java.util.ArrayList;

import static org.w3c.css.values.CssOperator.SPACE;

/**
 * @spec https://www.w3.org/TR/2016/CR-css-flexbox-1-20160526/#propdef-align-content
 * @spec https://www.w3.org/TR/2018/WD-css-align-3-20180423/#propdef-align-content
 */
public class CssAlignContent extends org.w3c.css.properties.css.CssAlignContent {

    public static final CssIdent baseline, normal;
    public static final CssIdent[] baseline_qualifier, content_distribution,
            overflow_position, content_position;

    static {
        baseline = CssIdent.getIdent("baseline");
        normal = CssIdent.getIdent("normal");

        String[] _baseline_qualifier_values = {"first", "last"};
        baseline_qualifier = new CssIdent[_baseline_qualifier_values.length];
        int i = 0;
        for (String s : _baseline_qualifier_values) {
            baseline_qualifier[i++] = CssIdent.getIdent(s);
        }
        i = 0;
        String[] _content_distribution_values = {"space-between", "space-around",
                "space-evenly", "stretch"};
        content_distribution = new CssIdent[_content_distribution_values.length];
        for (String s : _content_distribution_values) {
            content_distribution[i++] = CssIdent.getIdent(s);
        }
        i = 0;
        String[] _overflow_position_values = {"unsafe", "safe"};
        overflow_position = new CssIdent[_overflow_position_values.length];
        for (String s : _overflow_position_values) {
            overflow_position[i++] = CssIdent.getIdent(s);
        }
        i = 0;
        String[] _content_position_values = {"center", "start", "end",
                "flex-start", "flex-end"};
        content_position = new CssIdent[_content_position_values.length];
        for (String s : _content_position_values) {
            content_position[i++] = CssIdent.getIdent(s);
        }
    }

    public static CssIdent getBaselineQualifier(CssIdent ident) {
        for (CssIdent id : baseline_qualifier) {
            if (id.equals(ident)) {
                return id;
            }
        }
        return null;
    }

    public static CssIdent getContentDistribution(CssIdent ident) {
        for (CssIdent id : content_distribution) {
            if (id.equals(ident)) {
                return id;
            }
        }
        return null;
    }

    public static CssIdent getOverflowPosition(CssIdent ident) {
        for (CssIdent id : overflow_position) {
            if (id.equals(ident)) {
                return id;
            }
        }
        return null;
    }

    public static CssIdent getContentPosition(CssIdent ident) {
        for (CssIdent id : content_position) {
            if (id.equals(ident)) {
                return id;
            }
        }
        return null;
    }


    /**
     * Create a new CssAlignContent
     */
    public CssAlignContent() {
        value = initial;
    }

    /**
     * Creates a new CssAlignContent
     *
     * @param expression The expression for this property
     * @throws org.w3c.css.util.InvalidParamException
     *          Expressions are incorrect
     */
    public CssAlignContent(ApplContext ac, CssExpression expression, boolean check)
            throws InvalidParamException {
        if (check && expression.getCount() > 2) {
            throw new InvalidParamException("unrecognize", ac);
        }
        setByUser();

        value = parseAlignContent(ac, expression, this);
        if (!expression.end()) {
            throw new InvalidParamException("unrecognize", ac);
        }
    }

    public static CssValue parseAlignContent(ApplContext ac, CssExpression expression,
                                             CssProperty caller)
            throws InvalidParamException {
        CssValue val, value;
        ArrayList<CssValue> values = new ArrayList<>();
        char op;

        val = expression.getValue();
        op = expression.getOperator();

        if (val.getType() == CssTypes.CSS_IDENT) {
            CssIdent ident = (CssIdent) val;
            if (inherit.equals(ident)) {
                if (expression.getCount() > 1) {
                    throw new InvalidParamException("value", val.toString(),
                            caller.getPropertyName(), ac);
                }
                expression.next();
                return inherit;
            }
            if (normal.equals(ident)) {
                expression.next();
                return normal;
            }
            value = getContentDistribution(ident);
            if (value != null) {
                expression.next();
                return value;
            }
            // now try the two-values position, starting first with only one.
            if (baseline.equals(ident)) {
                expression.next();
                return baseline;
            }
            value = getContentPosition(ident);
            if (value != null) {
                expression.next();
                return value;
            }
            // ok, at that point we need two values.
            value = getBaselineQualifier(ident);
            if (value != null) {
                values.add(value);
                if (op != SPACE) {
                    throw new InvalidParamException("operator",
                            ((new Character(op)).toString()), ac);
                }
                expression.next();
                if (expression.end()) {
                    throw new InvalidParamException("unrecognize", ac);
                }
                val = expression.getValue();
                if (val.getType() != CssTypes.CSS_IDENT || !baseline.equals(val)) {
                    throw new InvalidParamException("value", val.toString(),
                            caller.getPropertyName(), ac);
                }
                values.add(baseline);
                expression.next();
                return new CssValueList(values);
            }
            value = getOverflowPosition(ident);
            if (value != null) {
                values.add(value);
                if (op != SPACE) {
                    throw new InvalidParamException("operator",
                            ((new Character(op)).toString()), ac);
                }
                expression.next();
                if (expression.end()) {
                    throw new InvalidParamException("unrecognize", ac);
                }
                val = expression.getValue();
                if (val.getType() != CssTypes.CSS_IDENT) {
                    throw new InvalidParamException("value", val.toString(),
                            caller.getPropertyName(), ac);
                }
                value = getContentPosition((CssIdent) val);
                if (value == null) {
                    throw new InvalidParamException("value", val.toString(),
                            caller.getPropertyName(), ac);
                }
                values.add(value);
                expression.next();
                return new CssValueList(values);
            }

        }
        throw new InvalidParamException("value",
                val.toString(),
                caller.getPropertyName(), ac);
    }

    public CssAlignContent(ApplContext ac, CssExpression expression)
            throws InvalidParamException {
        this(ac, expression, false);
    }

}

